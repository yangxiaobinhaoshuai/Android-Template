package com.yxb.gesture.example

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.example.gesture_example.R
import com.example.gesture_example.databinding.ActivityScaleGestureBinding
import com.yxb.simple.adapters.ScaleGestureListenerAdapter
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingActivity
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class ScaleGestureActivity : AbsViewBindingActivity<ActivityScaleGestureBinding>() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "ScaleGesture@@"

    private val imageView: ImageView by lazy { binding.imgvGestureActivity }


    override fun getActualBinding(): ActivityScaleGestureBinding =
        ActivityScaleGestureBinding.inflate(this.layoutInflater)

    override fun afterOnCreate() {
        super.afterOnCreate()

        val scaleGestureDetector =
            ScaleGestureDetector(this, object : ScaleGestureListenerAdapter() {

                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    logD(
                        """
                    ScaleGestureActivity 
                    onScale factor: ${detector.scaleFactor}, 
                    focusX: ${detector.focusX}, 
                    focusY: ${detector.focusY}, 
                """.trimIndent()
                    )

                    //val xScale = imageView.getObjAnim(animEnum = Anim.ScaleX, 1F, detector.scaleFactor)
                    //val yScale = imageView.getObjAnim(animEnum = Anim.ScaleY, 1F, detector.scaleFactor)
                    //(xScale + yScale).apply { this.duration = 0 }.start()

                    imageView.scaleX = detector.scaleFactor
                    imageView.scaleY = detector.scaleFactor

                    return super.onScale(detector)
                }

                override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                    logD(
                        """
                    ScaleGestureActivity 
                    onScaleBegin factor: ${detector.scaleFactor}, 
                    focusX: ${detector.focusX}, 
                    focusY: ${detector.focusY}, 
                """.trimIndent()
                    )
                    return super.onScaleBegin(detector)
                }

                override fun onScaleEnd(detector: ScaleGestureDetector) {
                    logD(
                        """
                    ScaleGestureActivity 
                    onScaleEnd factor: ${detector.scaleFactor}, 
                    focusX: ${detector.focusX}, 
                    focusY: ${detector.focusY}, 
                """.trimIndent()
                    )
                    super.onScaleEnd(detector)
                }

            })

        //imageView.doOnTouch { scaleGestureDetector.onTouchEvent(it) }
//        imageView.doOnInterceptTouch {
//            //logD("onTouch, eventX: ${it.x}, eventY: ${it.y}, pointers: ${it.pointerCount}")
//            scaleGestureDetector.onTouchEvent(it)
//        }
//        imageView.setOnTouchListener { v, event ->
//            //logD("Touched view :${event.action}")
//            return@setOnTouchListener scaleGestureDetector.onTouchEvent(event)
//        }

    }

}


class MyImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageView(context, attrs, defStyleAttr) {


    private val bitmap: Bitmap by lazy {
        val opts = BitmapFactory.Options()
        opts.outWidth = 100
        opts.outHeight = 200
        BitmapFactory.decodeResource(context.resources, R.drawable.xiaoxin_1242_x_1914, opts)
    }

    private val m = Matrix()

    private val p = Paint()

    private val logD = fun(m: String) = Log.d("ScaleGesture@@", m)

    private var scaleFactor by Delegates.observable(1F){ _: KProperty<*>, _: Float, newVal: Float ->
        logD("new scaleFactor :$newVal")
    }

    private val scaleGestureDetector =
        ScaleGestureDetector(this.context, object : ScaleGestureListenerAdapter() {

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                logD("custom image onScale, scaleF: ${detector.scaleFactor}")
                m.setScale(
                    detector.scaleFactor,
                    detector.scaleFactor,
                    this@MyImageView.scaleX + detector.focusX,
                    this@MyImageView.scaleY + detector.focusY
                )
                invalidate()
                return super.onScale(detector)
            }

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                logD("custom image onScaleBegin")
                return super.onScaleBegin(detector)
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
                logD("custom image onScaleEnd, scale: ${detector.scaleFactor}, w:${this@MyImageView.width}, h:${this@MyImageView.height}")
                scaleFactor *= detector.scaleFactor
                super.onScaleEnd(detector)
            }

        })

    private var initDraw = true

    init {
        this.setPadding(30,40,0,0)
        //this.doOnPreDraw { m.setTranslate(this.width / 2F - bitmap.width / 2F, this.height / 2F - bitmap.height / 2F) }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (initDraw) {
            canvas.save()
            canvas.translate(this.width / 2F - bitmap.width / 2F, this.height / 2F - bitmap.height / 2F)
            //m.setScale(2F, 2F, bitmap.width / 2F, bitmap.height / 2F)
            canvas.drawBitmap(bitmap, m, p)
            canvas.restore()
            initDraw = false
        } else {
            canvas.save()
            canvas.translate(this.scaleX, this.scaleY)
            canvas.drawBitmap(bitmap, m, p)
            canvas.restore()
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        return super.onTouchEvent(event)
        return scaleGestureDetector.onTouchEvent(event)
    }

}
