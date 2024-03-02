package me.yangxiaobin.canvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.logger.core.LogFacade

class CanvasFragment : AbsFragment(){

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "CanvasFragment"

    override val layoutResId: Int get() = R.layout.fragment_canvas

    private val customView by lazy { requireView().findViewById<View>(R.id.canvas_custom_view) }

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
    }

}

class CanvasCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val paint by lazy {
        val p = Paint()
        p.style = Paint.Style.STROKE
        p.strokeWidth = 2F
        p.color = HexColors.RED_400.colorInt
        p
    }

    private val emptyPaint = Paint()

    private val r1 = Rect(0, 0, 100, 100)
    private val r2 = Rect(20, 20, 150, 150)

    private val r3 = Rect(0,0,100,100)

    private val xfermode by lazy { PorterDuffXfermode(PorterDuff.Mode.SRC_IN) }

    private val txt = "12312312312312321"
    private val wholeScreen by lazy { RectF(0F,0F,width.toFloat(),height.toFloat()) }

    private val textBitmap: Bitmap by lazy {
        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        c.drawText(txt, 100F, 100F, emptyPaint)
        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        b
    }

    private val emptyBitmap: Bitmap by lazy {
        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        b
    }

    // TODO
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(textBitmap, 0F, 0F, emptyPaint)

    }
}
