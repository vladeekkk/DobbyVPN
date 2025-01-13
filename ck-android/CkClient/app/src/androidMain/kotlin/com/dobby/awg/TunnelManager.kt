package com.dobby.awg

import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.net.VpnService.Builder
import android.os.Build
import android.system.OsConstants
import com.dobby.awg.config.Config
import com.dobby.feature.vpn_service.AmneziaWGVpnService
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class TunnelManager(private val context: Context) {

    private val tunnelName = "awg0"
    var tunnelData: TunnelData =
        TunnelData(tunnelName, null, TunnelState.DOWN, -1)

    fun updateState(stringConfig: String?, state: TunnelState) {
        val config = if (stringConfig != null) Config.parse(stringConfig.byteInputStream()) else null

        if (state == TunnelState.UP) {
            if (config == null) throw RuntimeException("Empty config")

            if (VpnService.prepare(context) != null) throw RuntimeException("VPN_NOT_AUTHORIZED")

            if (!vpnService.isDone) {
                val vpnIntent = Intent(context, AmneziaWGVpnService::class.java)
                context.startService(vpnIntent)
            }

            val service: AmneziaWGVpnService
            try {
                service = vpnService.get(2, TimeUnit.SECONDS)
            } catch (e: TimeoutException) {
                throw RuntimeException(e)
            }

            if (tunnelData.currentTunnelHandle != -1) {
                // Tunnel already up
                return
            }

            // Build config
            val goConfig = config.toAwgUserspaceString()

            // Create the vpn tunnel with android API
            val builder: Builder = service.Builder()
            builder.setSession(tunnelName)

            for (addr in config.intface.addresses)
                builder.addAddress(addr.address, addr.mask)

            for (addr in config.intface.dnsServers)
                builder.addDnsServer(addr)

            for (dnsSearchDomain in config.intface.dnsSearchDomains)
                builder.addSearchDomain(dnsSearchDomain)

            var sawDefaultRoute = false
            for (peer in config.peers) {
                for (addr in peer.allowedIps) {
                    if (addr.mask == 0) sawDefaultRoute = true
                    builder.addRoute(addr.address, addr.mask)
                }
            }

            // "Kill-switch" semantics
            if (!(sawDefaultRoute && config.peers.size == 1)) {
                builder.allowFamily(OsConstants.AF_INET)
                builder.allowFamily(OsConstants.AF_INET6)
            }

            builder.setMtu(config.intface.mtu.orElse(1280))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) builder.setMetered(false)

            builder.setBlocking(true)
            val currentTunnelHandle: Int
            builder.establish().use { tun ->
                currentTunnelHandle =
                    GoBackendWrapper.awgTurnOn(tunnelName, tun!!.detachFd(), goConfig)
            }

            if (currentTunnelHandle < 0) throw RuntimeException("GO_ACTIVATION_ERROR")

            tunnelData = TunnelData(tunnelName, config, TunnelState.UP, currentTunnelHandle)

            service.protect(GoBackendWrapper.awgGetSocketV4(currentTunnelHandle))
            service.protect(GoBackendWrapper.awgGetSocketV6(currentTunnelHandle))
        } else {
            if (tunnelData.currentTunnelHandle == -1) {
                return
            }

            GoBackendWrapper.awgTurnOff(tunnelData.currentTunnelHandle)
            tunnelData = TunnelData(tunnelName, null, TunnelState.DOWN, -1)
        }
    }

    companion object {
        var vpnService: CompletableFuture<AmneziaWGVpnService> =
            CompletableFuture<AmneziaWGVpnService>()
    }
}