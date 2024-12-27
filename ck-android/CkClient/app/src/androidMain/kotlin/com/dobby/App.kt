package com.dobby

import android.app.Application
import com.dobby.di.startDI
import androidModule
import org.koin.android.ext.koin.androidContext

class DobbyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startDI(androidModule) { androidContext(applicationContext) }
    }
}
