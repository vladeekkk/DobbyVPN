package com.dobby.feature.vpn_service

import android.net.VpnService
import android.os.Build
import androidx.annotation.RequiresApi
import com.dobby.awg.TunnelManager

class AmneziaWGVpnService : VpnService() {

    override fun onCreate() {
        TunnelManager.vpnService.complete(this)
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onDestroy() {
        TunnelManager.vpnService = TunnelManager.vpnService.newIncompleteFuture();
        super.onDestroy()
    }
}
