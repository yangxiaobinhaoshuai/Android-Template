package me.yangxiaobin.android.ui.kit.samples

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.WrapContent
import me.yangxiaobin.android.kotlin.codelab.ext.WrapContentParams
import me.yangxiaobin.android.kotlin.codelab.ext.dp2px
import me.yangxiaobin.android.kotlin.codelab.ext.uicontroller.showFragmentToast
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.ui.kit.createShapeDrawable
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.logger.core.LogFacade

class EditTextFragment : AbsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "EditTextFragment"

    private lateinit var edt: EditText

    override fun createRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = createEmptyFrameLayout().apply {
        this.setBackgroundColor(HexColors.WHITE.colorInt)
        //val edt = requireContext().inflater.inflate(R.layout.edit_text_no_cursor_drawable,this,false) as EditText
        edt = EditText(requireContext())
        edt.hint = "12123123"

        // edt.setCursorDrawable(R.drawable.bg_cursor_blue.asDrawable())
        edt.setCursorDrawable(
            createShapeDrawable(
                solidColor = HexColors.PURPLE_A400.colorInt,
                sizeInPx = 4.dp2px.toInt() to 0.dp2px.toInt(),
                topPaddingInPx = -10,
                bottomPaddingInPx = -10,
            )
        )

        val lp = FrameLayout.LayoutParams(WrapContent, WrapContent).apply { this.gravity = Gravity.CENTER }
        this.addView(edt, lp)

        val edt1 = EditText(requireContext())
        val edt1Lp = FrameLayout.LayoutParams(WrapContent, WrapContent).apply { this.topMargin = 60 }
        this.addView(edt1, edt1Lp)

        val bt = Button(requireContext())
        val btLp = WrapContentParams
        bt.text = "Change color"

        bt.setOnClickListener {
            showFragmentToast("Change color")

            edt1.requestFocus()

        }

        this.addView(bt,btLp)


    }

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

    }

    private fun EditText.setCursorDrawable(d: Drawable?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            // FIXME  这个方法只能修改 cursor 一次
            this.textCursorDrawable = d

            //val editor = EditText::class.java.getDeclaredField("mEditor")
        } else {
            try {
                // FIXME
                // 1.
//                val mCursorDrawable = TextView::class.java.getDeclaredField("mCursorDrawable")
//                mCursorDrawable.isAccessible = true
//                mCursorDrawable.set(this, d)

                // 2.
                val mCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
                mCursorDrawableRes.isAccessible = true
                mCursorDrawableRes.set(this, 0)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        this.invalidate()

    }

}
