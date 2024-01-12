package me.yangxiaobin.android.embedding_compat

import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.children
import androidx.core.view.doOnPreDraw
import com.yxb.simple.adapters.OnGestureListenerAdapter
import com.yxb.simple.adapters.OnSingleTapListener
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.MarginLayoutParams
import me.yangxiaobin.android.kotlin.codelab.ext.NormalLayoutParams
import me.yangxiaobin.android.kotlin.codelab.ext.uicontroller.showFragmentToast
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.kotlin.codelab.log.L
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.common_ui.EmptyFragment
import me.yangxiaobin.common_ui.MyFrameLayout
import me.yangxiaobin.logger.core.LogFacade

class GestureDetectorFragment : EmptyFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "GestureDetectorFragment"

    private val gd by lazy {
        GestureDetectorCompat(requireContext(), object : OnGestureListenerAdapter() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                //showFragmentToast("onSingleTapUp")
                return super.onSingleTapUp(e)
            }

            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)
                showFragmentToast("longPress")
            }
        })
    }

    //override fun getRootContainer(context: Context): FrameLayout = MyFrameLayout(context)

    @SuppressLint("ClickableViewAccessibility")
    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
        view.setBackgroundColor(HexColors.GRAY_300.colorInt)

        (view as ViewGroup).children.forEach {
            it.setOnTouchListener { _, event ->
                gd.onTouchEvent(event)
            }
        }

        //addCover()

    }

    private fun addCover(){
        requireView().doOnPreDraw {
            val coverLp = MarginLayoutParams(400,300)
            coverLp.topMargin = 900
            coverLp.leftMargin = 1050
            val cover = FrameLayout(requireContext())
            cover.layoutParams = coverLp
            cover.setBackgroundColor(HexColors.GREEN_400.colorInt)
            cover.setWillNotDraw(false)
            cover.isClickable =  true

            (it as ViewGroup).addView(cover)
        }
    }

    override fun customChildren(): List<View> {

        val edt = EditText(requireContext())
        edt.hint = "hihihihi"
        val edtId = View.generateViewId()
        edt.id = edtId

        return listOf(edt)
    }

    override fun onButtonClick(index: Int, v: View) {
        super.onButtonClick(index, v)
        when (index) {
            0 -> {}
            1 -> {}
            2 -> {}
            else -> Unit
        }
    }
}
