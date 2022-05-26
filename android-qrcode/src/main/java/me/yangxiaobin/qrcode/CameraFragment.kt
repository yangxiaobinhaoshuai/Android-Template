package me.yangxiaobin.qrcode

import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import bindView
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.MatchParentParams
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment
import me.yangxiaobin.logger.core.LogFacade

class CameraFragment : AbsComposableFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "CameraScanFragment"

    private val previewId = View.generateViewId()

    private val previewView: PreviewView by bindView(previewId)

    override val composableContent = @Composable {
        AndroidCameraXPreView()
    }

    @Composable
    fun AndroidCameraXPreView() {
        AndroidView(factory = { context ->
            logD("PreviewView init.")

            PreviewView(context).apply {
                this.id = previewId
                this.layoutParams = MatchParentParams
                this.setBackgroundColor(HexColors.YELLOW_600.colorInt)
            }

        })

    }


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
        startCamera()
    }


    // ProcessCameraProvider.getInstance(context);
    private val cameraProviderFuture by lazy { CameraXConfigProvider.getInstance(requireContext()) }

    /**
     * start to preview.
     *
     * implement preview use case : https://developer.android.com/codelabs/camerax-getting-started#3
     */
    private fun startCamera() {

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            bindPreview(cameraProvider)

        }, ContextCompat.getMainExecutor(requireContext()))

    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        // Preview
        val preview = Preview.Builder()
            .build()

        val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview)

        preview.setSurfaceProvider(previewView.surfaceProvider)

    }


}
