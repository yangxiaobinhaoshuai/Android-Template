package me.yangxiaobin.canvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
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

    private val bitmapCanvas by lazy {
        Canvas(emptyBitmap)
    }

    private val watermarkPaint by lazy {
        val p = Paint()
        p.color = Color.BLACK
        p.textSize = 30F
        p
    }

    // TODO
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //canvas.drawBitmap(textBitmap, 0F, 0F, emptyPaint)
//        val waterText = "你好我是水印"
//        bitmapCanvas.drawText(waterText, 50F, 50F, watermarkPaint)

        //canvas.drawBitmap(text2Bitmap("12312313"),200F,200F,watermarkPaint)

        val path = Path()
        path.moveTo(300f, 150f)
        path.cubicTo(300f, 50f, 200f, 50f, 200f, 150f)
        path.cubicTo(200f, 250f, 300f, 350f, 300f, 350f)
        path.cubicTo(300f, 350f, 400f, 250f, 400f, 150f)
        path.cubicTo(400f, 50f, 300f, 50f, 300f, 150f)
        //path.close()

        val list = path.toList()
        val toPath = list.toPath()

        //canvas.drawPath(path,paint)
        //canvas.drawPath(toPath,paint)

        //val path = Path()

//        val centerX = 200f
//        val centerY = 200f
//        val radius = 100f
//
//        path.addCircle(centerX, centerY, radius, Path.Direction.CW)

        //canvas.drawPath(path, paint)

        val pathMeasure = PathMeasure(toPath, false)
        val pathLength = pathMeasure.length

        val divisions = 4*4 // 将圆分成的弧线数量
        val anglePerDivision = 360f / divisions

        for (i in 5 until divisions) {
            val startAngle = i * anglePerDivision
            val endAngle = startAngle + anglePerDivision

            val startRatio = startAngle / 360f
            val endRatio = endAngle / 360f

            val startDistance = startRatio * pathLength
            val endDistance = endRatio * pathLength

            val segment = Path()
            pathMeasure.getSegment(startDistance, endDistance, segment, true)
            if (i == 1) segment.moveTo(300f, 150f)


            //if (i % 2 == 0)
            //if (i  == 0 || i == 3)
            canvas.drawPath(segment, paint)

            // 处理每个切分得到的弧线，例如绘制或其他操作
        }

    }

    private fun List<PointF>.toPath(close: Boolean = true): Path {
        val path = Path()

        for (element in this) {
            val p: PointF = element
            val x: Float = p.x
            val y: Float = p.y
            path.lineTo(x, y)
        }
        //if (close) path.close()

        return path
    }


    private fun Path.toList():List<PointF>{
        val path = this // 假设你有一个已经构建好的 Path 对象

        val pathMeasure = PathMeasure(path, false)
        val pathLength = pathMeasure.length

        val segmentLength = 10f // 设置路径上的采样间隔，你可以根据需要调整这个值

        val point = FloatArray(2)
        val points = mutableListOf<PointF>()

        var distance = 0f
        while (distance < pathLength) {
            pathMeasure.getPosTan(distance, point, null)
            val x = point[0]
            val y = point[1]
            Log.d("CanvasFragment", "x: $x, y: $y.")
            points.add(PointF(x, y))

            distance += segmentLength
        }
        return points
    }

    private fun text2Bitmap(
        text: String,
        textSize: Float = 30F,
        textColor: Int = Color.RED,
        typeface: Typeface? = null,
    ): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            setTextSize(textSize)
            color = textColor
            setTypeface(typeface)
        }

        val textBounds = Rect()
        paint.getTextBounds(text, 0, text.length, textBounds)

        val width = textBounds.width()
        val height = textBounds.height()

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawText(text, -textBounds.left.toFloat(), -textBounds.top.toFloat(), paint)

        return bitmap
    }

}
