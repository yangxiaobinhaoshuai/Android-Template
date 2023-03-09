package me.yangxiaobin.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.logger.core.LogFacade

class RectFragment : AbsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "RectFragment@@"

    override val layoutResId: Int get() = R.layout.fragment_rect


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

    }
}


class RectImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val paint by lazy {
        val p = Paint()
        p.style = Paint.Style.STROKE
        p.strokeWidth = 2F
        p.color = HexColors.RED_400.colorInt
        p
    }

    private val r1 = Rect(0, 0, 100, 100)
    private val r2 = Rect(20, 20, 150, 150)

    private val r3 = Rect(0,0,100,100)

    private val xfermode by lazy { PorterDuffXfermode(PorterDuff.Mode.SRC_IN) }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 会影响在此之后的区域
        canvas.clipRect(r3)

        canvas.drawRect(r1, paint)

        paint.color = HexColors.GREEN_400.colorInt
        paint.xfermode = xfermode
        canvas.drawRect(r2, paint)
    }
}