import com.dobby.logging.presentation.LogsViewModel
import com.dobby.main.presentation.MainViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val sharedModule = module {
    singleOf(::LogsViewModel)
    singleOf(::MainViewModel)
}
