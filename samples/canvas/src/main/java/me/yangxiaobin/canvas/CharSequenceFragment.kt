package me.yangxiaobin.canvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.provider.CalendarContract.Colors
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.ui.kit.createShapeDrawable
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.logger.core.LogFacade

class CharSequenceFragment : AbsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "CharSequenceFragment@@"

    override val layoutResId: Int get() = R.layout.fragment_char_sequence

    val tv by lazy { requireView().findViewById<TextView>(R.id.tv_char_sequence) }
    val imgv by lazy { requireView().findViewById<ImageView>(R.id.imgv_char_sequence) }

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)


        tv.background = createShapeDrawable(borderColor = HexColors.RED_400.colorInt,)

        val txt = "123123213123"
        val paint = TextPaint()
        paint.textSize = 30F
        val tvWidth = 100
        val staticLayout = StaticLayout.Builder.obtain(txt, 0, txt.length, paint, tvWidth).build()
        val bmp = Bitmap.createBitmap(240, 240, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawText(txt, 0, txt.length, 0F, 0F, paint)
        staticLayout.draw(canvas)

        imgv.setImageBitmap(bmp)

    }
}


/**
 * TODO unused.
 */
class CharSequenceImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
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

    private val r3 = Rect(0, 0, 100, 100)

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
