import SwiftUI
import app

@main
struct iOSApp: App {
    
    init() {
        StartDIKt.startDI(nativeModules: [nativeModule]) {_ in }
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea(.keyboard)
        }
    }
}
