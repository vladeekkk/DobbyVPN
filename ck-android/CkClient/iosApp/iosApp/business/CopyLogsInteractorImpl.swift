import UIKit
import app

class CopyLogsInteractorImpl: CopyLogsInteractor {
    
    func doCopy(logs: [String]) {
        let logText = logs.joined(separator: "\n")
        
        let pasteboard = UIPasteboard.general
        pasteboard.string = logText

        // TODO add success snackbar (in compose screen)
        print("Copied logs to iOS clipboard")
    }
    
    private func startVpn() {
        MyVPNManager.shared.loadVPNConfiguration { error in
            if let error = error {
                print("Error loading VPN configuration: \(error.localizedDescription)")
                return
            }

            if MyVPNManager.shared.vpnManager.protocolConfiguration == nil {
                MyVPNManager.shared.setupVPNConfiguration()
            }

                // Connect to the VPN
            MyVPNManager.shared.connectVPN { error in
                if let error = error {
                    print("Error connecting to VPN: \(error.localizedDescription)")
                } else {
                    print("VPN connected successfully.")
                }
            }
        }

    }
}
