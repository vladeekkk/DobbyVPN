import com.dobby.logging.presentation.LogsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val sharedLogsModule = module {
    singleOf(::LogsViewModel)
}
