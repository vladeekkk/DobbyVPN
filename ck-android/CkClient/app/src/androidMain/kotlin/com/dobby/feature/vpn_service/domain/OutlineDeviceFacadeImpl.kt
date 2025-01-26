package com.dobby.feature.vpn_service.domain

import cloak_outline.Cloak_outline
import cloak_outline.OutlineDevice
import com.dobby.feature.vpn_service.OutlineDeviceFacade

class OutlineDeviceFacadeImpl: OutlineDeviceFacade {

    private var device: OutlineDevice? = null

    override fun init(apiKey: String) {
        device = Cloak_outline.newOutlineDevice(apiKey)
    }

    override fun write(data: ByteArray) {
        device?.write(data)
    }

    override fun read(): ByteArray? {
        return device?.read()
    }
}
