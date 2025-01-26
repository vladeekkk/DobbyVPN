package com.dobby.feature.vpn_service

interface OutlineDeviceFacade {

    fun init(apiKey: String)

    fun write(data: ByteArray)

    fun read(): ByteArray?
}