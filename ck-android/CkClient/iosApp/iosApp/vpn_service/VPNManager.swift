import Foundation
import NetworkExtension

class MyVPNManager {
    
    private var dobbyBundleIdentifier = "vpn.dobby.app.iosVpnTest"
    private var dobbyName = "DobbyVPN"
    
    private var vpnManager: NETunnelProviderManager!

    func setup(onSuccess: (() -> ())? = nil) {
        getExistingManager { existingManager in
            if let manager = existingManager {
                self.vpnManager = manager
                print("Using existing manager: \(manager)")
                onSuccess?()
            } else {
                self.makeManager().saveToPreferences { error in
                    if let error = error {
                        print("Error saving VPN configuration: \(error)")
                    } else {
                        print("VPN configuration saved successfully")
                        onSuccess?()
                    }
                }
            }
        }
    }
    
    private func makeManager() -> NETunnelProviderManager {
        vpnManager = NETunnelProviderManager()
        vpnManager.localizedDescription = dobbyName

        let proto = NETunnelProviderProtocol()
        proto.providerBundleIdentifier = dobbyBundleIdentifier
        proto.serverAddress = "127.0.0.1:4009"
        proto.providerConfiguration = [:]
        vpnManager.protocolConfiguration = proto
        vpnManager.isEnabled = true
        return vpnManager
    }
    
    func connectVPN(completion: @escaping (Error?) -> Void) {
        do {
            try vpnManager.connection.startVPNTunnel()
            completion(nil)
        } catch {
            completion(error)
        }
    }
    
    func disconnectVPN(completion: @escaping (Error?) -> Void) {
        vpnManager.connection.stopVPNTunnel()
        completion(nil)
    }
    
    private func getExistingManager(completion: @escaping (NETunnelProviderManager?) -> Void) {
        NETunnelProviderManager.loadAllFromPreferences { (managers, error) in
            let manager = managers?.first(where: { $0.localizedDescription == self.dobbyName })
            manager!.loadFromPreferences { error in
                if let error = error {
                    print("Error loading preferences: \(error)")
                } else {
                    do {
                        print("completion(manager)")
                        completion(manager)
                    }
                }
            }
        }
    }
}
