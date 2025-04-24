package com.dobby.feature.main.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class PermissionEventsChannel {

    private val checkPermissionsEvents = MutableSharedFlow<Unit>()

    private val permissionsGrantedEvents = MutableSharedFlow<Boolean>()

    fun observeCheckPermissionsEvents(): Flow<Unit> {
        return checkPermissionsEvents
    }

    suspend fun checkPermissions() {
        checkPermissionsEvents.emit(Unit)
    }

    fun observePermissionGrantedEvents(): Flow<Boolean> {
        return permissionsGrantedEvents
    }

    suspend fun onPermissionGranted(isGranted: Boolean) {
        permissionsGrantedEvents.emit(isGranted)
    }
}
