package com.dobby.feature.vpn_service.domain

import cloak_outline.Cloak_outline
import cloak_outline.OutlineDevice
import com.dobby.feature.vpn_service.OutlineLibFacade

internal class OutlineLibFacadeImpl: OutlineLibFacade {

    private var device: OutlineDevice? = null

    override fun init(apiKey: String) {
        device = Cloak_outline.newOutlineDevice(apiKey)
    }

    override fun writeData(data: ByteArray) {
        device?.write(data)
    }

    override fun readData(): ByteArray? {
        return device?.read()
    }
}
