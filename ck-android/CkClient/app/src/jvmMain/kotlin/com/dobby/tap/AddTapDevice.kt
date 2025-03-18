import java.io.File

fun updatePath() {
    val currentPath = System.getenv("PATH")
    val newPath = "$currentPath;${System.getenv("SystemRoot")}\\system32;${System.getenv("SystemRoot")}\\system32\\wbem;${System.getenv("SystemRoot")}\\system32\\WindowsPowerShell\\v1.0"
    System.setProperty("java.library.path", newPath)

    println("Updated PATH: $newPath")
}

fun addTapDevice(appDir: String) {
    val deviceName = "outline-tap0"
    val deviceHwid = "tap0901"
    val tapInstallPath = "tap-windows6/tapinstall.exe"
    val oemVistaPath = "tap-windows6/OemVista.inf"

    updatePath()
    // Checking if a TAP device exists
    if (executeCommand("netsh interface show interface name=$deviceName") == 0) {
        println("TAP network device already exists.")
        configureTapDevice(deviceName)
        return
    }
    println("Creating TAP network device...")
    runAsAdmin(appDir, "$tapInstallPath install $oemVistaPath $deviceHwid")

    // Find new TAP device name (we should change it to outline-tap0)
    val tapName = findTapDeviceName()
    if (tapName.isNullOrEmpty()) {
        println("Could not find TAP device name.")
        return
    }
    println("Found TAP device name: $tapName")

    // Rename TAP device
    if (executeCommand("netsh interface set interface name=\"$tapName\" newname=\"$deviceName\"") != 0) {
        println("Could not rename TAP device.")
        return
    }

    configureTapDevice(deviceName)
}

fun configureTapDevice(deviceName: String) {
    println("Configuring TAP device subnet...")
    if (executeCommand("netsh interface ip set address $deviceName static 10.0.85.2 255.255.255.255") != 0) {
        println("Could not set TAP network device subnet.")
        return
    }

    println("Configuring primary DNS...")
    if (executeCommand("netsh interface ip set dnsservers $deviceName static address=1.1.1.1") != 0) {
        println("Could not configure TAP device primary DNS.")
        return
    }

    println("Configuring secondary DNS...")
    if (executeCommand("netsh interface ip add dnsservers $deviceName 9.9.9.9 index=2") != 0) {
        println("Could not configure TAP device secondary DNS.")
        return
    }
}

fun executeCommand(command: String): Int {
    return try {
        val process = ProcessBuilder("cmd.exe", "/c", command).start()
        process.waitFor()
    } catch (e: Exception) {
        println("Error executing command: $command\n${e.message}")
        -1
    }
}


fun runAsAdmin(appDir: String, command: String) {
    val processBuilder = ProcessBuilder(
        "powershell",
        "-Command",
        "Start-Process powershell -WindowStyle Hidden -ArgumentList \"-NoProfile;  $command\" -Verb RunAs"
    )

    try {
        val process = processBuilder
            .redirectErrorStream(true)
            .directory(File(appDir))
            .start()

        process.inputStream.bufferedReader().use { reader ->
            println(reader.readText())
        }

        process.waitFor()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

