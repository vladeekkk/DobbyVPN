import Foundation
import app

class LocalLogsRepository : LogsRepository {

    private let logsFileURL: URL

    init() {

        let documentDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
        logsFileURL = documentDirectory.appendingPathComponent("logs.txt")

        if !FileManager.default.fileExists(atPath: logsFileURL.path) {
            FileManager.default.createFile(atPath: logsFileURL.path, contents: nil, attributes: nil)
        }
    }

    func writeLog(log: String) {
        do {
            let existingLogs = try String(contentsOf: logsFileURL, encoding: .utf8)
            let newLogs = existingLogs + "\n" + log
            try newLogs.write(to: logsFileURL, atomically: true, encoding: .utf8)
        } catch {
            print("Error writing log: \(error)")
        }
    }

    func readLogs() -> [String] {
        do {
            let logs = try String(contentsOf: logsFileURL, encoding: .utf8)
            return logs.split(separator: "\n").map { String($0) }
        } catch {
            print("Error reading logs: \(error)")
            return []
        }
    }

    func clearLogs() {
        do {
            try "".write(to: logsFileURL, atomically: true, encoding: .utf8)
        } catch {
            print("Error clearing logs: \(error)")
        }
    }
}
