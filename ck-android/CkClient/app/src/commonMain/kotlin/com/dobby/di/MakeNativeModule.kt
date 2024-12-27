import com.dobby.logging.domain.CopyLogsInteractor
import com.dobby.logging.domain.LogsRepository
import com.dobby.main.domain.VpnManager
import com.dobby.main.domain.ConnectionStateRepository
import com.dobby.main.domain.DobbyConfigsRepository
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module

typealias NativeInjectionFactory<T> = Scope.() -> T

fun makeNativeModule(
    copyLogsInteractor: NativeInjectionFactory<CopyLogsInteractor>,
    logsRepository: NativeInjectionFactory<LogsRepository>,
    configsRepository: NativeInjectionFactory<DobbyConfigsRepository>,
    connectionStateRepository: NativeInjectionFactory<ConnectionStateRepository>,
    vpnManager: NativeInjectionFactory<VpnManager>,
): Module {
    return module {
        factory { vpnManager() }
        single { copyLogsInteractor() }
        single { logsRepository() }
        single { connectionStateRepository() }
        single { configsRepository() }
    }
}
