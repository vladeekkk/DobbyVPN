package com.example.ck_client

import android.content.Context
import com.dobby.main.domain.VpnManager

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
