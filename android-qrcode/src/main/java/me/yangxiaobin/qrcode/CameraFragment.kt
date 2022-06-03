package me.yangxiaobin.qrcode

import android.annotation.SuppressLint
import android.media.Image
import android.view.View
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import bindView
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.MatchParentParams
import me.yangxiaobin.android.kotlin.codelab.ext.showFragmentToast
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment
import me.yangxiaobin.logger.core.LogFacade
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

    @SuppressLint("UnsafeOptInUsageError")
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
        val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
            //.setTargetResolution(Size(width, height))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

//        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy: ImageProxy ->
//            logD("imageAnalysis.callback, $imageProxy.")
//            ServiceProvider.getServiceOrNull(QrCodeUtilityProvider::class.java)?.readImageProxy(imageProxy)
//        }


        // mlkit : https://developers.google.com/ml-kit/vision/barcode-scanning/android
        val options: BarcodeScannerOptions = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC,
            )
            .build()

        val scanner: BarcodeScanner = BarcodeScanning.getClient(options)


        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy: ImageProxy ->

            val mediaImage: Image = imageProxy.image ?: kotlin.run { imageProxy.close();return@setAnalyzer }

            val image: InputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val res: Task<MutableList<Barcode>> = scanner.process(image)
                .addOnSuccessListener { barCodes: MutableList<Barcode>->
                    if (barCodes.size > 0){
                        //listener.invoke(barCodes[0],imageProxy.width,imageProxy.height)

                        for (barcode in barCodes) {
                            val bounds = barcode.boundingBox
                            val corners = barcode.cornerPoints

                            val rawValue = barcode.rawValue

                            val valueType: Int = barcode.valueType
                            // See API reference for complete list of supported types
                            when (valueType) {
                                Barcode.TYPE_WIFI -> {
                                    val ssid = barcode.wifi!!.ssid
                                    val password = barcode.wifi!!.password
                                    val type = barcode.wifi!!.encryptionType
                                }
                                Barcode.TYPE_URL -> {
                                    val title = barcode.url!!.title
                                    val url = barcode.url!!.url

                                    logD("scan successful, title: $title, url: $url.")
                                    showFragmentToast("QrCode for url type, title:$title, url:$url.")
                                }
                            }
                        }
                        //接收到结果后，就关闭解析
                        scanner.close()
                    }
                }
                .addOnFailureListener {
                    logD("scan fail : ${it.message}.")
                }
                .addOnCompleteListener { it: Task<MutableList<Barcode>> ->
                    logD("scan completed.")
                    imageProxy.close()
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
