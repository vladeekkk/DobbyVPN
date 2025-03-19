import java.io.BufferedReader

fun findTapDeviceName(): String? {
    val netAdaptersClassGuid = "{4D36E972-E325-11CE-BFC1-08002BE10318}"
    val netAdaptersKey = "HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Control\\Class\\$netAdaptersClassGuid"
    val netConfigKey = "HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Control\\Network\\$netAdaptersClassGuid"

    val findCommand = "reg query $netAdaptersKey /s /f \"tap0901\" /e /d"
    val adapters = executeCommandForFind(findCommand).lines()
        .filter { it.matches(Regex("HKEY.*\\\\\\d{4}\$")) }

    if (adapters.isEmpty()) {
        println("Can't find TAP device register")
        return null
    }

    var latestTimestamp = "0"
    var adapterName: String? = null

    for (adapterKey in adapters) {

        val netConfigId = queryRegistryValue(adapterKey, "NetCfgInstanceId")
        if (netConfigId == null) {
            println("Can't find NetCfgInstanceId for $adapterKey.")
            continue
        }

        val installTimestamp = queryRegistryValue(adapterKey, "InstallTimeStamp")
        if (installTimestamp == null) {
            println("Can't find InstallTimeStamp for $adapterKey.")
            continue
        }

        val nameKey = "$netConfigKey\\$netConfigId\\Connection"
        val name = queryRegistryValue(nameKey, "Name", multipleTokens = true)
        if (name == null) {
            println("Adapter hasn't got name: $adapterKey.")
            continue
        }

        if (installTimestamp > latestTimestamp) {
            latestTimestamp = installTimestamp
            adapterName = name
        }
    }

    return adapterName
}

private fun queryRegistryValue(key: String, valueName: String, multipleTokens: Boolean = false): String? {
    val command = "reg query \"$key\" /v \"$valueName\""
    val output = executeCommandForFind(command)

    if (output.isBlank()) {
        println("Key \"$key\" isn't find or empty")
        return null
    }

    val line = output.lines().find { it.contains(valueName) } ?: return null
    val tokens = line.split(Regex("\\s+"))

    return if (multipleTokens) {
        tokens.drop(3).joinToString(" ")
    } else {
        tokens.getOrNull(3)
    }
}

private fun executeCommandForFind(command: String): String {
    val process = ProcessBuilder(command.split(" ")).redirectErrorStream(true).start()
    return process.inputStream.bufferedReader().use(BufferedReader::readText).also {
        process.waitFor()
    }
}
