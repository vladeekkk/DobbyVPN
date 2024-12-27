package com.dobby.feature.main.domain

import android.content.Context
import com.dobby.feature.vpn_service.MyVpnService

class VpnManagerImpl(
    private val context: Context,
): VpnManager {

    override fun start() {
        MyVpnService
            .createIntent(context)
            .let(context::startService)
    }

    override fun stop() {
        val vpnServiceIntent = MyVpnService.createIntent(context)
        context.startService(vpnServiceIntent)
        context.stopService(vpnServiceIntent)
    }
}
