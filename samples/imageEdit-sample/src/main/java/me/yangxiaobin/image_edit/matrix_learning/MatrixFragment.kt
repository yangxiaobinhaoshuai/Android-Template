package me.yangxiaobin.image_edit.matrix_learning

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogFun
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.ui.kit.createShapeDrawable
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.image_edit.R
import me.yangxiaobin.logger.core.LogFacade

/**
 * Matrix 用法总结： https://blog.csdn.net/jdsjlzx/article/details/52741445
 *
 * Android 图像变换原理 Matrix ：https://blog.csdn.net/pathuang68/article/details/6991867
 *
 * 掘金 Matrix ：https://juejin.cn/post/6844903507577815053
 *
 * Android 图片旋转基于 Matrix ： https://cloud.tencent.com/developer/article/1726676
 *
 * 简书 Android 旋转剪切： https://www.jianshu.com/p/d9d469b42eb2
 */
class MatrixFragment : AbsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "MatrixFragment"

    /**
     * ImageView 200 * 400 dp
     */
    override val layoutResId: Int get() = R.layout.fragment_matrix

    override fun beforeViewReturned(view: View): View {
        return super.beforeViewReturned(view)
            .also { it.setBackgroundColor(HexColors.YELLOW_600.colorInt) }
            .also {
                it.findViewById<ImageView>(R.id.imgv_matrix)?.background =
                    createShapeDrawable(
                        strokeWidthInDp = 1,
                        borderColor = HexColors.BLUE_100.colorInt
                    )
            }
    }

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
        val imageView = view.findViewById<MyImageView>(R.id.imgv_matrix)
        imageView.post{
            logD("ImageView size : ${imageView.width} - ${imageView.height}")
        }
        imageView.logD = this.logD
    }
}

class MyImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val mMatrix: Matrix = Matrix()

    private val rectMatrix = Matrix()

    init {
        // 设置为 单位阵
        //mMatrix.reset()

        //mMatrix.setRotate(100F)

        // mMatrix.setRotate(40F)

        //mMatrix.setScale(2F,2F)

        // 向下平移
        //mMatrix.setTranslate(200F,200F)

        //mMatrix.mapRect()

//        rectMatrix.setScale(2F,2F)
        rectMatrix.preRotate(45F,250F,300F)
       // rectMatrix.setRotate(90F,100F,1000F)

    }

    private val bitmap: Bitmap by lazy {
        // 241 * 160
        BitmapFactory.decodeResource(this.context.resources, R.drawable.android_robot_black_850_1108)
    }
    private val paint by lazy { Paint() }

    private val borderPaint by lazy {
        Paint().apply {
            this.color = HexColors.RED_500.colorInt
            this.style = Paint.Style.STROKE
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //val src = Rect(100,100,600,600)
        val dst = RectF(100F,100F,400F,500F)

        val border = Rect(0,0,bitmap.width,bitmap.height)

        logD("bitmap size: ${bitmap.width},  ${bitmap.height}")

        rectMatrix.mapRect(dst)

        val bitmapMatrix = Matrix()
        bitmapMatrix.postRotate(90F)
        val rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,bitmapMatrix,true)
//        canvas.drawBitmap(rotatedBitmap, null, dst, paint)

        canvas.drawBitmap(bitmap, null, dst, paint)
        //canvas.drawBitmap(bitmap, mMatrix, paint)


//        canvas.drawBitmap(bitmap, mMatrix, paint)

        canvas.drawRect(border, borderPaint)

        borderPaint.color = HexColors.BLUE_500.colorInt
        canvas.drawRect(dst, borderPaint)
    }

    lateinit var logD:LogFun
}
