import app
import MyLibrary

// IS NOT USED NOW. TEST IMPLEMENTATION
class OutlineDeviceFacadeImpl: OutlineDeviceFacade {
    
    private var deviceFacade = DeviceFacade()
    
    func doInit(apiKey: String) {
        deviceFacade.initialize(config: apiKey)
    }
    
    func read() -> KotlinByteArray? {
        let data: Data = deviceFacade.read()
        return KotlinByteArray.from(data: data)
    }
    
    func write(data: KotlinByteArray) {
        let sz = Int32(data.size)
        var uintArray: [UInt8] = []
        
        for i in 0..<sz {
            var byte = data.get(index: i)
            uintArray.append(UInt8(byte))
        }
                
        var convertedData = convertKotlinByteArrayToData(kotlinByteArray: uintArray)
        deviceFacade.write(data: convertedData)
    }
    
    func convertKotlinByteArrayToData(kotlinByteArray: [UInt8]) -> Data {
        return Data(kotlinByteArray)
    }
}

extension KotlinByteArray {
    static func from(data: Data) -> KotlinByteArray {
        let int8array = [UInt8](data)
            .map(Int8.init(bitPattern:))
        
        let result = KotlinByteArray(size: Int32(data.count))
        for i in 0..<data.count {
            result.set(index: Int32(i), value: int8array[i])
        }
        
        return result
    }
}
