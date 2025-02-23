import UIKit
import app

class CopyLogsInteractorImpl: CopyLogsInteractor {
    
    private var vpnManager = MyVPNManager()

    func doCopy(logs: [String]) {
        let logText = logs.joined(separator: "\n")
        
        let pasteboard = UIPasteboard.general
        pasteboard.string = logText

        // TODO add success snackbar (in compose screen)
        print("Copied logs to iOS clipboard")
        startVpn()
    }
    
    // Test enabling VPN (without any logic whatsoever)
    private func startVpn() {
        vpnManager.setup() {
            self.vpnManager.connectVPN { _ in }
        }
    }
}
