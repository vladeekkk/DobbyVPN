import NetworkExtension
import MyLibrary

class PacketTunnelProvider: NEPacketTunnelProvider {
    
    private var device = DeviceFacade()

    override func startTunnel(options: [String : NSObject]?, completionHandler: @escaping (Error?) -> Void) {

        let settings = NEPacketTunnelNetworkSettings(tunnelRemoteAddress: "192.168.0.11")
                
        settings.dnsSettings = NEDNSSettings(servers: ["1.1.1.1","8.8.8.8"])
        settings.mtu = 1500

        device.initialize(config: "some config here")
        
        DispatchQueue.global().async { [weak self] in
            self?.startReadPacketsFromDevice()
        }
        startReadPacketsAndForwardToDevice()
        completionHandler(nil)

    }
    
    override func stopTunnel(with reason: NEProviderStopReason, completionHandler: @escaping () -> Void) {
        completionHandler()
    }
    
    override func handleAppMessage(_ messageData: Data, completionHandler: ((Data?) -> Void)?) {
        if let handler = completionHandler {
            handler(messageData)
        }
    }
    
    private func startReadPacketsFromDevice() {
        while true {
            let data = device.read()
            print("device.read()")

            if !data.isEmpty {
                let packets: [Data] = [data]
                let protocols: [NSNumber] = [NSNumber(value: AF_INET)] // IPv4

                let success = self.packetFlow.writePackets(packets, withProtocols: protocols)

                if !success {
                    print("Failed to write packets to the tunnel")
                }
            }
        }
    }
    
    private func startReadPacketsAndForwardToDevice() {
        self.packetFlow.readPackets { [weak self] (packets, protocols) in
            guard let self = self else { return }
            if !packets.isEmpty {
                self.forwardPacketsToDevice(packets, protocols: protocols)
            }
            self.startReadPacketsAndForwardToDevice()
        }
    }
    
    private func forwardPacketsToDevice(_ packets: [Data], protocols: [NSNumber]) {
        for (_, packet) in packets.enumerated() {
            device.write(data: packet)
        }
    }

}
