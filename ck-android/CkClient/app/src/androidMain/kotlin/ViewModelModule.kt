import com.dobby.logging.presentation.LogsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

actual val sharedLogsModule = module {
    viewModelOf(::LogsViewModel)
}
