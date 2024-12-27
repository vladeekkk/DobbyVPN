import SwiftUI
import app

@main
struct iOSApp: App {
    
    init() {
        StartDIKt.startDI(nativeModule: nativeModule) {_ in }
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea(.keyboard)
        }
    }
}
