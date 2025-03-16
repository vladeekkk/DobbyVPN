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
}
