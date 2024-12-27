import android.content.Context.MODE_PRIVATE
import com.dobby.domain.DobbyConfigsRepositoryImpl
import com.dobby.logs.CopyLogsInteractorImpl
import com.dobby.logs.LogsRepositoryImpl
import com.dobby.main.domain.ConnectionStateRepository
import com.example.ck_client.VpnManagerImpl
import org.koin.android.ext.koin.androidContext

val androidModule = makeNativeModule(
    copyLogsInteractor = { CopyLogsInteractorImpl(get()) },
    logsRepository = { LogsRepositoryImpl({ androidContext().filesDir }) },
    configsRepository = {
        DobbyConfigsRepositoryImpl(
            prefs = androidContext().getSharedPreferences("DobbyPrefs", MODE_PRIVATE)
        )
    },
    connectionStateRepository = { ConnectionStateRepository },
    vpnManager = { VpnManagerImpl(androidContext()) }
)
