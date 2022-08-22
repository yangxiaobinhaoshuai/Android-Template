package me.yangxiaobin.android.codelab.recyclerview

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import me.yangxiaobin.android.codelab.R
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.getActionString
import me.yangxiaobin.android.kotlin.codelab.ext.isOnView
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.kotlin.codelab.recyclerview.AbsVH
import me.yangxiaobin.android.kotlin.codelab.recyclerview.SimpleRvAdapter
import me.yangxiaobin.logger.core.LogFacade


typealias OnMotionEventAction = (View?) -> Unit


class NormalGridRvFragment : AbsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "NormalGridRvFragment"

    override val layoutResId: Int = R.layout.fragment_recyclerview

    private val rv: RecyclerView by lazy { rv_fragment }


    @SuppressLint("ClickableViewAccessibility")
    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        val mockList = mutableListOf<Int>()
        repeat(30) { mockList += it }

        rv.layoutManager = GridLayoutManager(requireContext(), 4)
        rv.adapter = SimpleRvAdapter(
            mockList,
            android.R.layout.simple_list_item_1
        ) { (vh: AbsVH, _, pos, _) ->

            vh.requireView<TextView>(android.R.id.text1).run {
                gravity = Gravity.CENTER
                text = mockList[pos].toString()
            }


            vh.itemView.setOnTouchListener { _: View, motionEvent: MotionEvent ->
                logD("grid item onTouch: ${motionEvent.getActionString}")
                true
            }

        }

        setupClickEffectViaFunc()
        //setupClickEffectDirectly()
    }

    private fun setupClickEffectDirectly() {
        rv.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {

            private var downView: View? = null

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

                logD("click effect: ${e.getActionString}")

                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downView = rv.children.find { e.isOnView(it) }
                        downView?.alpha = 0.3F
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> downView?.alpha = 1F
                    else -> Unit
                }

                return super.onInterceptTouchEvent(rv, e)
            }
        })
    }

    private fun setupClickEffectViaFunc() {
        rv.setClickEffect(down = { it?.alpha = 0.3F }, cancel = { it?.alpha = 1F })
    }


    /**
     * 可以被提取为 API
     * 设置 Rv item 点击特效，如：press alpha = 0.6 , normal alpha = 1.0
     */
    private fun RecyclerView.setClickEffect(
        down: OnMotionEventAction = {},
        cancel: OnMotionEventAction = {}
    ) = apply {

        this.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {

            private var downView: View? = null

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downView = rv.children.find { e.isOnView(it) }
                        down.invoke(downView)
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        cancel.invoke(downView)
                        downView = null
                    }
                    else -> Unit
                }

                return super.onInterceptTouchEvent(rv, e)
            }
        })
    }

}
