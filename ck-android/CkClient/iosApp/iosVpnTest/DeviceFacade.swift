import MyLibrary

class DeviceFacade {

    private var device: Cloak_outlineOutlineDevice? = nil

    func initialize(config: String) {
        device = Cloak_outlineOutlineDevice(config)
    }
    
    func write(data: Data) {
        do {
           try device?.write(data, ret0_: nil)
        } catch let error {
            print("error is \(error)")
        }
    }
    
    func read() -> Data {
        do {
            return try (device?.read())!
        } catch let error {
            print("error is \(error)")
            return Data()
        }
    }
    
    func close() {
        do {
            try device?.close()
        } catch {}
        device = nil
    }
}
