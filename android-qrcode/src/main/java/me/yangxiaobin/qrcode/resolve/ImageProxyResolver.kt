package me.yangxiaobin.qrcode.resolve

import androidx.camera.core.ImageProxy

interface ImageProxyResolver {

    fun resolve(imageProxy: ImageProxy)

}
