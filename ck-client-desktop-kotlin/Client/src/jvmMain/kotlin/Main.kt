import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.MainScreen
import java.io.File
import java.io.IOException
import java.net.URLDecoder

fun main() = application {

    Runtime.getRuntime().addShutdownHook(Thread {
        val logFile = File("logs.txt")
        if (logFile.exists()) {
            logFile.writeText("")
        }
    })

    Window(onCloseRequest = ::exitApplication, title = "Dobby VPN") {
        MainScreen()
    }
}