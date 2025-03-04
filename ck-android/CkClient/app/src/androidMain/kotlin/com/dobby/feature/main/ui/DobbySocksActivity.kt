package com.dobby.feature.main.ui

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.dobby.feature.main.domain.ConnectionStateRepository
import com.dobby.feature.main.presentation.MainViewModel
import com.dobby.navigation.App
import com.dobby.util.Logger
import com.dobby.common.ui.theme.CkClientTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DobbySocksActivity : ComponentActivity() {

    private lateinit var requestVpnPermissionLauncher: ActivityResultLauncher<Intent>

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Logger.init(this)
        ConnectionStateRepository.init(false)

        initVpnPermissionLauncher()
        lifecycleScope.launch {
            viewModel.checkVpnPermissionEvents.collect { checkVpnPermissionAndStart() }
        }

        setContent {
            CkClientTheme {
                App(
                    modifier = Modifier,
                    mainViewModel = viewModel
                )
            }
        }
    }

    private fun checkVpnPermissionAndStart() {
        val vpnIntent = VpnService.prepare(this)
        if (vpnIntent != null) {
            requestVpnPermissionLauncher.launch(vpnIntent)
        } else {
            // not the best way to do it,
            viewModel.checkPermissionAndStartVpn(isGranted = true)
        }
    }

    private fun initVpnPermissionLauncher() {
        requestVpnPermissionLauncher = registerForActivityResult(
            StartActivityForResult()
        ) { result ->
            viewModel.checkPermissionAndStartVpn(isGranted = result.resultCode == RESULT_OK)
        }
    }
}
