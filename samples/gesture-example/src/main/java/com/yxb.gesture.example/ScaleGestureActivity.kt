package com.yxb.gesture.example

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.example.gesture_example.databinding.ActivityScaleGestureBinding
import com.yxb.simple.adapters.ScaleGestureListenerAdapter
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingActivity
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.animator.Anim
import me.yangxiaobin.android.kotlin.codelab.ext.animator.getObjAnim
import me.yangxiaobin.android.kotlin.codelab.ext.animator.plus
import me.yangxiaobin.android.kotlin.codelab.ext.doOnInterceptTouch
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

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

//                    val xScale = imageView.getObjAnim(animEnum = Anim.ScaleX, 1F, detector.scaleFactor)
//                    val yScale = imageView.getObjAnim(animEnum = Anim.ScaleY, 1F, detector.scaleFactor)
//                    (xScale + yScale).apply { this.duration = 0 }.start()

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
        imageView.doOnInterceptTouch {
            //logD("onTouch, eventX: ${it.x}, eventY: ${it.y}, pointers: ${it.pointerCount}")
            scaleGestureDetector.onTouchEvent(it)
        }
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

    val logD = fun(m: String) = Log.d("ScaleGesture@@", m)

    private val scaleGestureDetector =
        ScaleGestureDetector(this.context, object : ScaleGestureListenerAdapter() {

            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                logD("custom image onScale")
                return super.onScale(detector)
            }

            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                logD("custom image onScaleBegin")
                return super.onScaleBegin(detector)
            }

            override fun onScaleEnd(detector: ScaleGestureDetector?) {
                logD("custom image onScaleEnd")
                super.onScaleEnd(detector)
            }

        })

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
//        return scaleGestureDetector.onTouchEvent(event)
    }

}
