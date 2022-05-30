package me.yangxiaobin.qrcode_impl

import me.yangxiaobin.module_service_provider_annotation.ModuleService
import me.yangxiaobin.qrcode.impl.QrCodeUtilityProvider

@ModuleService(QrCodeUtilityProvider::class)
internal class QrCodeImpl : QrCodeUtilityProvider {

    override fun readImageProxy(imageProxy: androidx.camera.core.ImageProxy) {
        println("----> I'm impl of QrCodeUtilityProvider...")
    }

}
