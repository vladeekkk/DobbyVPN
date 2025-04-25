package com.dobby.feature.main.domain

import android.content.Context
import com.dobby.feature.vpn_service.DobbyVpnService

class VpnManagerImpl(
    private val context: Context,
): VpnManager {

    override fun start() {
        DobbyVpnService
            .createIntent(context)
            .let(context::startService)
    }

    override fun stop() {
        val vpnServiceIntent = DobbyVpnService.createIntent(context)
        context.startService(vpnServiceIntent)
        context.stopService(vpnServiceIntent)
    }
}
