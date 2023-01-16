package com.yxb.gesture.example

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper
import com.example.gesture_example.databinding.FragmentViewDragHelperBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingFragment
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogFun
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class ViewDragHelperFragment : AbsViewBindingFragment<FragmentViewDragHelperBinding>() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "ViewDragHelperFragment"

    private val tv: TextView by lazy { binding.tvViewDragHelper }

    override fun getActualBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentViewDragHelperBinding =
        FragmentViewDragHelperBinding.inflate(inflater, container, false)


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

    }

}

class MyConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    logD: LogFun = {}
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val dragger by lazy {
        ViewDragHelper.create(this, 1F, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                logD("dragger tryCaptureView")
                return true
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                //return super.clampViewPositionHorizontal(child, left, dx)
                return left
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                //return super.clampViewPositionVertical(child, top, dy)
                return top
            }

        })
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        //return super.onInterceptTouchEvent(ev)
        return dragger.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //return super.onTouchEvent(event)
        dragger.processTouchEvent(event)
        return true
    }
}