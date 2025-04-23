package com.dobby.feature.main.domain

class AwgManagerImpl(): AwgManager {

    override fun getAwgVersion(): String { return "AwgVersion" }
    override fun onAwgConnect() {}

    override fun onAwgDisconnect() {}

}
