package com.dobby.feature.vpn_service.domain

import cloak_outline.Cloak_outline
import com.dobby.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

object CloakConnectionInteractor {

    private val isConnected = AtomicBoolean(false)

    // TODO idk why this exists, refactor later
    private val connectionsCounter = AtomicInteger(0)

    suspend fun connect(
        config: String,
        localHost: String = "127.0.0.1",
        localPort: String = "1984",
    ): ConnectResult {
        if (config.isEmpty() || localHost.isEmpty() || localPort.isEmpty()) {
            return ConnectResult.ValidationError
        }
        Logger.log("Attempting to connect cloak")
        return withContext(Dispatchers.IO) {
            if (isConnected.compareAndSet(false, true)) {
                val result = runCatching {
                    if (connectionsCounter.incrementAndGet() == 1) {
                        Cloak_outline.startCloakClient(localHost, localPort, config, false)
                    } else {
                        Cloak_outline.startAgain()
                    }
                }
                if (result.isSuccess) {
                    ConnectResult.Success
                } else {
                    ConnectResult.Error(result.exceptionOrNull()!!)
                }
            } else {
                ConnectResult.AlreadyConnected
            }
        }
    }

    suspend fun disconnect(): DisconnectResult {
        Logger.log("Attempting to disconnect cloak")
        return withContext(Dispatchers.IO) {
            if (isConnected.compareAndSet(true, false)) {
                val result = runCatching { Cloak_outline.stopCloak() }
                if (result.isSuccess) {
                    DisconnectResult.Success
                } else {
                    DisconnectResult.Error(result.exceptionOrNull()!!)
                }
            } else {
                DisconnectResult.AlreadyDisconnected
            }
        }
    }
}
