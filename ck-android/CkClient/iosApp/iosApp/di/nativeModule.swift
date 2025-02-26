import app

var nativeModule: Koin_coreModule = MakeNativeModuleKt.makeNativeModule(
  copyLogsInteractor: { scope in
    return CopyLogsInteractorImpl()
  },
  logsRepository: { scope in
      return LocalLogsRepository()
  },
  configsRepository: { scope in
      return DobbyConfigsRepositoryImpl()
  },
  connectionStateRepository: { scope in
      return ConnectionStateRepository()
  },
  vpnManager: { scope in
      return VpnManagerImpl()
  }
)
