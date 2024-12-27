package com.dobby.feature.vpn_service.domain

sealed interface ConnectResult {
    object ValidationError : ConnectResult
    object Success : ConnectResult
    object AlreadyConnected : ConnectResult
    class Error(val error: Throwable) : ConnectResult
}

sealed interface DisconnectResult {
    object Success : DisconnectResult
    object AlreadyDisconnected : DisconnectResult
    class Error(val error: Throwable) : DisconnectResult
}
