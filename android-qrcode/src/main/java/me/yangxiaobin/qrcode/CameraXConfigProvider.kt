package me.yangxiaobin.qrcode

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import com.google.common.util.concurrent.ListenableFuture

object CameraXConfigProvider {

    private var configured = false

    @SuppressLint("UnsafeOptInUsageError")
    @Synchronized
    fun getInstance(context: Context): ListenableFuture<ProcessCameraProvider> {
        if (!configured) {
            configured = true
            ProcessCameraProvider
                .configureInstance(
                    CameraXConfig.Builder
                        .fromConfig(Camera2Config.defaultConfig())
                        .setMinimumLoggingLevel(Log.ERROR)
//                    .setCameraExecutor(myExecutor)
//                    .setSchedulerHandler(mySchedulerHandler)
                        .build()
                )
        }

        return ProcessCameraProvider.getInstance(context)
    }
}
