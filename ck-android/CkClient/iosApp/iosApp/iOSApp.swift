import SwiftUI
import app

@main
struct iOSApp: App {
    
    init() {
        StartDIKt.startDI(
            nativeModule: nativeModule,
            appDeclaration: { _ in }
        )

        // TODO remove this testing
        let logsRepository = LocalLogsRepository()

        logsRepository.writeLog(log: "App started successfully.")

        let logs = logsRepository.readLogs()
        print(logs)

        logsRepository.clearLogs()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea(.keyboard)
        }
    }
}
