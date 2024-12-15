import com.dobby.logs.CopyLogsInteractorImpl
import com.dobby.logs.LogsRepositoryImpl
import org.koin.android.ext.koin.androidContext

val nativeModule = makeNativeModule(
    copyLogsInteractor = { CopyLogsInteractorImpl(get()) },
    logsRepository = { LogsRepositoryImpl({ androidContext().filesDir }) }
)
