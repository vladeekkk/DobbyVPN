package com.dobby

import android.app.Application
import com.dobby.di.startDI
import androidMainModule
import androidVpnModule
import org.koin.android.ext.koin.androidContext

class DobbyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startDI(listOf(androidMainModule, androidVpnModule)) {
            androidContext(applicationContext)
        }
    }
}
