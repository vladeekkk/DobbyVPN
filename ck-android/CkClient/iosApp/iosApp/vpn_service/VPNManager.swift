import Foundation
import NetworkExtension

class MyVPNManager {
    
    // 1. Load Existing VPN Configuration
    // 2. Setup VPN Configuration (if not exists)
    // 3. Save VPN Configuration
    // 4. Conntect to the VPN
    // 5. Disconnect from the VPN
    // 6. Check VPN Connection Status
    
    static let shared = MyVPNManager()
    
    let vpnManager: NEVPNManager = NEVPNManager.shared()
    
    let manager = NETunnelProviderManager()
    
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
        vpnManager.isEnabled = true
        let packetTunnelProtocol = NETunnelProviderProtocol()
        packetTunnelProtocol.providerBundleIdentifier = "OutlineTunnelProvider"
        packetTunnelProtocol.serverAddress = "10.111.222.1"

        packetTunnelProtocol = 1500

        // You cannot directly set IP addresses and DNS servers using NEVPNProtocolPacketTunnel;
            // Instead, you must handle those in your PacketTunnelProvider implementation.
        // Example of setting DNS servers is shown in the PacketTunnelProvider implementation
        
        self.vpnManager.protocolConfiguration = packetTunnelProtocol
        self.vpnManager.isEnabled = true
        self.vpnManager.saveToPreferences { error in
            if let error = error {
                print("Error saving VPN configuration: \(error)")
            } else {
                print("VPN configuration saved successfully")
            }
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
