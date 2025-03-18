package com.dobby.feature.main.domain

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.firstOrNull

object ConnectionStateRepository {

    private val connectionFlow = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        connectionFlow.tryEmit(false)
    }

    fun update(isConnected: Boolean) {
        connectionFlow.tryEmit(isConnected)
    }

    fun observe(): Flow<Boolean> {
        return connectionFlow
    }

    suspend fun isConnected(): Boolean {
        return connectionFlow.firstOrNull() ?: false
    }
}
