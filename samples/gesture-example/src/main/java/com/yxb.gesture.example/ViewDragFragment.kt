package com.yxb.gesture.example

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.example.gesture_example.databinding.FragmentViewDragBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingFragment
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.doOnInterceptTouch
import me.yangxiaobin.android.kotlin.codelab.ext.doOnTouch
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.makeDraggable
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class ViewDragFragment : AbsViewBindingFragment<FragmentViewDragBinding>() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "ViewDragFragment"

    private val tv: TextView by lazy { binding.tvViewDrag }

    override fun getActualBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentViewDragBinding = FragmentViewDragBinding.inflate(inflater, container, false)

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        var dx: Float = 0F
        var dy: Float = 0F
        tv.doOnInterceptTouch { event: MotionEvent ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dx = tv.x - event.rawX
                    dy = tv.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    tv.animate()
                        .x(event.rawX + dx)
                        .y(event.rawY + dy)
                        .setDuration(0)
                        .start()
                }
                else -> {
                    dx = 0F
                    dy = 0F
                }
            }

            return@doOnInterceptTouch false
        }

//        tv.makeDraggable()

    }
}

/**
 * @see https://stackoverflow.com/a/31094315/10247834
 */
class MyTv @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    var dx: Float = 0F
    var dy: Float = 0F

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dx = this.x - event.rawX
                dy = this.y - event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                this.animate()
                    .x(event.rawX + dx)
                    .y(event.rawY + dy)
                    .setDuration(0)
                    .start()
            }
            else -> {
                dx = 0F
                dy = 0F
            }
        }

        //return super.onTouchEvent(event)
        return true
    }

}