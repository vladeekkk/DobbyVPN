import Foundation
import app

public class LocalLogsRepository : LogsRepository {

    private let logFileName = "logs.txt"
    
    private var sharedContainerURL: URL? {
        return FileManager.default.containerURL(forSecurityApplicationGroupIdentifier: appGroupIdentifier)
    }
    
    public init() {}

    public func writeLog(log: String) {
        guard let sharedContainerURL = sharedContainerURL else {
            NSLog("Error: Unable to access shared container URL.")
            return
        }
        
        let logFileURL = sharedContainerURL.appendingPathComponent(logFileName)
        
        do {
            var existingLogs = readLogs()
            existingLogs.append(log)
            
            let logText = existingLogs.joined(separator: "\n")
            try logText.write(to: logFileURL, atomically: true, encoding: .utf8)
        } catch {
            NSLog("Error writing log: \(error)")
            print("Error writing log: \(error)")
        }
    }

    public func readLogs() -> [String] {
        guard let sharedContainerURL = sharedContainerURL else {
            print("Error: Unable to access shared container URL.")
            return []
        }
        
        let logFileURL = sharedContainerURL.appendingPathComponent(logFileName)
        
        if !FileManager.default.fileExists(atPath: logFileURL.path) {
            NSLog("Log file does not exist. Returning empty logs.")
            print("Log file does not exist. Returning empty logs.")
            return []
        }
        
        do {
            // Read the log file content
            let logText = try String(contentsOf: logFileURL, encoding: .utf8)
            return logText.components(separatedBy: "\n")
        } catch {
            NSLog("Error reading logs: \(error)")
            return []
        }
    }
    
    public func clearLogs() {
        do {
            try "".write(to: sharedContainerURL!, atomically: true, encoding: .utf8)
        } catch {
            print("Error clearing logs: \(error)")
        }
    }
}
