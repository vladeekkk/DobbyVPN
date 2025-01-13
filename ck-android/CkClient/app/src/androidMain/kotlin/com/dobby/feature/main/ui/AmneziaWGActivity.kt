package com.dobby.feature.main.ui

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.dobby.awg.GoBackendWrapper
import com.dobby.awg.TunnelManager
import com.dobby.awg.TunnelState
import com.dobby.common.ui.theme.CkClientTheme
import com.dobby.feature.vpn_service.AmneziaWGVpnService
import com.dobby.navigation.AmneziaWGApp
import kotlinx.coroutines.launch

class AmneziaWGActivity : ComponentActivity() {

    private lateinit var requestVpnPermissionLauncher: ActivityResultLauncher<Intent>
    private val tunnelManager = TunnelManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initVpnPermissionLauncher()
        lifecycleScope.launch {
            checkVpnPermissionAndStart()
        }

        setContent {
            CkClientTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        AmneziaWGApp(
                            Modifier.padding(innerPadding),
                            ::onConnect,
                            ::onDisconnect
                        ) {
                            Text(
                                text = "AmneziaWG ${GoBackendWrapper.awgVersion()}",
                                fontSize = 24.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp)
                            )
                        }
                    }
                )
            }
        }
    }

    private fun onConnect(config: String) {
        tunnelManager.updateState(config, TunnelState.UP)
        Toast.makeText(
            this,
            "Connected ${tunnelManager.tunnelData.name} : ${tunnelManager.tunnelData.currentTunnelHandle}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onDisconnect() {
        tunnelManager.updateState(null, TunnelState.DOWN)
        Toast.makeText(
            this,
            "Disconnected ${tunnelManager.tunnelData.name} : ${tunnelManager.tunnelData.currentTunnelHandle}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun checkVpnPermissionAndStart() {
        val vpnIntent = VpnService.prepare(this)
        if (vpnIntent != null) {
            requestVpnPermissionLauncher.launch(vpnIntent)
        } else {
            startService(Intent(this, AmneziaWGVpnService::class.java))
        }
    }

    private fun initVpnPermissionLauncher() {
        requestVpnPermissionLauncher = registerForActivityResult(
            StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                startService(Intent(this, AmneziaWGVpnService::class.java))
            }
        }
    }

}
