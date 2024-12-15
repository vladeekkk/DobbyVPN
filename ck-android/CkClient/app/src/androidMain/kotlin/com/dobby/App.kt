package com.dobby

import android.app.Application
import com.dobby.di.startDI
import nativeModule
import org.koin.android.ext.koin.androidContext

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startDI(nativeModule) { androidContext(applicationContext) }
    }
}
