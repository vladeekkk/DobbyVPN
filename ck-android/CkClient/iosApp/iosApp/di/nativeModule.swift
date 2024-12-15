import app

var nativeModule: Koin_coreModule = MakeNativeModuleKt.makeNativeModule(
  copyLogsInteractor: { scope in
    return CopyLogsInteractorImpl()
  },
  logsRepository: { scope in
      return LocalLogsRepository()
  }
)
