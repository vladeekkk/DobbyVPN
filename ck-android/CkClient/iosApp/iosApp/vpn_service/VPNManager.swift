import Foundation
import NetworkExtension

class VPNManager {
    
    static let shared = VPNManager()
    
    private var vpnManager: NEVPNManager
    
    private init() {
        vpnManager = NEVPNManager.shared()
    }
    
    func loadVPNConfiguration(completion: @escaping (Error?) -> Void) {
        vpnManager.loadFromPreferences { error in
            if let error = error {
                completion(error)
            } else {
                completion(nil)
            }
        }
    }
    
    func setupVPNConfiguration(serverAddress: String, username: String, password: String) {

        // Непонятно, как зарегистрировать OutlineTunnelProvider здесь.

        let vpnProtocol = NEPacketTunnelProvider()
        vpnManager.isEnabled = true
        
        saveVPNConfiguration { error in
            if let error = error {
                print("Error saving VPN configuration: \(error)")
            } else {
                print("VPN configuration saved successfully")
            }
        }
    }
    
    private func saveVPNConfiguration(completion: @escaping (Error?) -> Void) {
        vpnManager.saveToPreferences { error in
            completion(error)
        }
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
    
    func isVPNConnected() -> Bool {
        return vpnManager.connection.status == .connected
    }
}
