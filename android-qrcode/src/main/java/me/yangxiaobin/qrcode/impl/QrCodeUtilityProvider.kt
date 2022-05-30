package me.yangxiaobin.qrcode.impl

import androidx.camera.core.ImageProxy

interface QrCodeUtilityProvider {

    fun readImageProxy(imageProxy: ImageProxy)

}
