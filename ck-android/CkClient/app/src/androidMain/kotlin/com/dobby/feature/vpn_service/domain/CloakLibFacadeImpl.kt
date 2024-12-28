package com.dobby.feature.vpn_service.domain

import com.dobby.feature.vpn_service.CloakLibFacade
import cloak_outline.Cloak_outline

class CloakLibFacadeImpl : CloakLibFacade {

    override fun startClient(localHost: String, localPort: String, config: String) {
        Cloak_outline.startCloakClient(localHost, localPort, config, false)
    }

    override fun stopClient() {
        Cloak_outline.stopCloak()
    }

    override fun restartClient() {
        Cloak_outline.startAgain()
    }
}
