import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.MainScreen
import java.io.File
import java.io.IOException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun main() = application {
    // Получение текущего пути к jar-файлу
    val encodedPath = this::class.java.protectionDomain.codeSource.location.path
    val decodedPath = URLDecoder.decode(encodedPath, StandardCharsets.UTF_8.name())
    val appDir = File(decodedPath).parentFile // Директория приложения
    val batFilePath = File(appDir, "add_tap_device.bat").absolutePath

    val batFile = File(batFilePath)
    if (batFile.exists()) {
        try {
            // Запускаем bat файл
            val process = ProcessBuilder(batFile.absolutePath, appDir.absolutePath)
                .inheritIO() // Для того чтобы вывод был виден в консоли
                .start()

            // Ожидаем завершения процесса
            val exitCode = process.waitFor()
            println("Process exited with code $exitCode")
        } catch (e: IOException) {
            println("Failed to start the process: ${e.message}")
        } catch (e: InterruptedException) {
            println("The process was interrupted: ${e.message}")
        }
    } else {
        println("The file add_tap_device.bat does not exist in the current directory.")
    }

    // Очистка логов при завершении приложения
    Runtime.getRuntime().addShutdownHook(Thread {
        val logFile = File("logs.txt")
        if (logFile.exists()) {
            logFile.writeText("Очистка логов после завершения работы приложения.")
        }
    })

    // Отображение основного окна приложения
    Window(onCloseRequest = ::exitApplication, title = batFile.exists().toString()) {
        MainScreen()
    }
}
