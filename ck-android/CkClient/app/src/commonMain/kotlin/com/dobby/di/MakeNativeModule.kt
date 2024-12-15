import com.dobby.logging.domain.CopyLogsInteractor
import com.dobby.logging.domain.LogsRepository
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module

typealias NativeInjectionFactory<T> = Scope.() -> T

fun makeNativeModule(
    copyLogsInteractor: NativeInjectionFactory<CopyLogsInteractor>,
    logsRepository: NativeInjectionFactory<LogsRepository>
): Module {
    return module {
        single { copyLogsInteractor() }
        single { logsRepository() }
    }
}
