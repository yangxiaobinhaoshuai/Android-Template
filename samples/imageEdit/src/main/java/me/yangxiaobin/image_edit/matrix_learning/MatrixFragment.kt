package me.yangxiaobin.image_edit.matrix_learning

import android.content.Context
import android.graphics.*
import android.telephony.ims.feature.MmTelFeature.MmTelCapabilities
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.ui.kit.createShapeDrawable
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.image_edit.R

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

    }
}

class MyImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val mMatrix: Matrix = Matrix()

    init {
        // 设置为 单位阵
        //mMatrix.reset()

        //mMatrix.setRotate(100F)

        // 向下平移
        //mMatrix.setTranslate(200F,200F)


        // mMatrix.setRotate(40F)

        mMatrix.setScale(2F,2F)

        //mMatrix.mapRect()

    }

    private val bitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(this.context.resources, R.drawable.ic_android_logo)
    }
    private val paint by lazy { Paint() }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, mMatrix, paint)
    }
}