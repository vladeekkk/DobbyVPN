import NetworkExtension
import Foundation

class OutlineTunnelProvider: NEPacketTunnelProvider {
    
    private var device = DeviceFacade()
    private var readFromDeviceQueue: DispatchQueue?
    
    override func startTunnel(options: [String : NSObject]?, completionHandler: @escaping (Error?) -> Void) {

        device.initialize(config: "here is config")
        
        readFromDeviceQueue = DispatchQueue(label: "qq")
        
        readFromDeviceQueue?.async { [weak self] in
            self?.readPacketsLoop()
        }
        
        startReadPacketsAndForwardToDevice()
        completionHandler(nil)
    }
    
    override func stopTunnel(with reason: NEProviderStopReason, completionHandler: @escaping () -> Void) {
        completionHandler()
    }
    
    private func readPacketsLoop() {
        while true {
            let data = device.read()

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
        // Read packets from the tunnel
        self.packetFlow.readPackets { [weak self] (packets, protocols) in
            guard let self = self else { return }
            if !packets.isEmpty {
                self.forwardPacketsToDevice(packets, protocols: protocols)
            }
            self.startReadPacketsAndForwardToDevice()
        }
    }
    private func forwardPacketsToDevice(_ packets: [Data], protocols: [NSNumber]) {
        for (index, packet) in packets.enumerated() {
            device.write(data: packet)
        }
    }
}
