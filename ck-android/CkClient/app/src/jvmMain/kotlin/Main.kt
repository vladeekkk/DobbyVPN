import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dobby.di.startDI
import com.dobby.feature.main.presentation.MainViewModel
import com.dobby.navigation.App
import com.sun.jna.Platform
import org.koin.mp.KoinPlatform
import java.io.File
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun getMainViewModel(): MainViewModel = KoinPlatform.getKoin().get()

fun main() = application {
    startDI(listOf(jvmMainModule, jvmVpnModule)){}
    // Получение текущего пути к jar-файлу
    val encodedPath = this::class.java.protectionDomain.codeSource.location.path
    val decodedPath = URLDecoder.decode(encodedPath, StandardCharsets.UTF_8.name())
    val appDir = File(decodedPath).parentFile.absolutePath
    if (Platform.isWindows()) {
        // start device check
        addTapDevice(appDir)
    }

    // Очистка логов при завершении приложения
    Runtime.getRuntime().addShutdownHook(Thread {
        val logFile = File("logs.txt")
        if (logFile.exists()) {
            logFile.writeText("Очистка логов после завершения работы приложения.")
        }
    })

    // Launch the main window and call your shared App composable.
    Window(onCloseRequest = ::exitApplication, title = "Dobby VPN 13") {
        App(mainViewModel = getMainViewModel())
    }
}