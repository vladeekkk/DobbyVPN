import app
import Foundation

class DobbyConfigsRepositoryImpl: DobbyConfigsRepository {
    
    private let cloakConfigKey = "cloakConfigKey"
    private let isCloakEnabledKey = "isCloakEnabledKey"
    private let outlineConfigKey = "outlineConfigKey"
    private let isOutlineEnabledKey = "isOutlineEnabledKey"
    
    func getCloakConfig() -> String {
        return UserDefaults.standard.string(forKey: cloakConfigKey) ?? ""
    }
    
    func setCloakConfig(newConfig: String) {
        UserDefaults.standard.set(newConfig, forKey: cloakConfigKey)
    }
    
    func getIsCloakEnabled() -> Bool {
        return UserDefaults.standard.bool(forKey: isCloakEnabledKey)
    }

    func setIsCloakEnabled(isCloakEnabled: Bool) {
        UserDefaults.standard.set(isCloakEnabled, forKey: isCloakEnabledKey)
    }
    
    func getOutlineKey() -> String {
        return UserDefaults.standard.string(forKey: outlineConfigKey) ?? ""
    }
    
    func setOutlineKey(newOutlineKey: String) {
        UserDefaults.standard.set(newOutlineKey, forKey: outlineConfigKey)
    }
    
    func getIsOutlineEnabled() -> Bool {
        return UserDefaults.standard.bool(forKey: isOutlineEnabledKey)
    }

    
    func setIsOutlineEnabled(isOutlineEnabled: Bool) {
        UserDefaults.standard.set(isOutlineEnabled, forKey: isOutlineEnabledKey)
    }
}
