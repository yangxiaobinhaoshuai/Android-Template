package me.yangxiaobin.qrcode

import android.view.View
import androidx.camera.core.*
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
import me.yangxiaobin.module_service_provider.ServiceProvider
import me.yangxiaobin.qrcode.impl.QrCodeUtilityProvider
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CameraFragment : AbsComposableFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "CameraScanFragment"

    private val previewId = View.generateViewId()

    private val previewView: PreviewView by bindView(previewId)

    override val handleBackPress: Boolean = false

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

        val screenAspectRatio: Int = aspectRatio(previewView.width, previewView.height)
        logD( "Preview aspect ratio: $screenAspectRatio")

        // Preview
        val preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .build()

        // imageAnalysis
        val imageAnalysis = ImageAnalysis.Builder()
            .build()
            .apply {
                setAnalyzer(Executors.newSingleThreadExecutor()){ imageProxy: ImageProxy ->
                    logD("imageAnalysis.callback, $imageProxy.")

                    ServiceProvider.getServiceOrNull(QrCodeUtilityProvider::class.java)?.readImageProxy(imageProxy)

                }
            }

        try {// A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)

            // Attach the viewfinder's surface provider to preview use case
            preview.setSurfaceProvider(previewView.surfaceProvider)
        } catch (e: Exception) {
            e.printStackTrace()
            logE("Camera use case bind failed, $e.")
        }

    }


    /**
     *  [androidx.camera.core.ImageAnalysis.Builder] requires enum value of
     *  [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     *
     *  Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     *  of preview ratio to one of the provided values.
     *
     *  @param width - preview width
     *  @param height - preview height
     *  @return suitable aspect ratio
     */
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    companion object{
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

    }

}
