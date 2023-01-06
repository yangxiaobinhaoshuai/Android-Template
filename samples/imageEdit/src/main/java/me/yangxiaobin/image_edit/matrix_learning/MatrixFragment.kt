package me.yangxiaobin.image_edit.matrix_learning

import android.view.View
import android.widget.ImageView
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.ui.kit.createShapeDrawable
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.image_edit.R

class MatrixFragment : AbsFragment() {

    override val layoutResId: Int get() = R.layout.fragment_matrix


    override fun beforeViewReturned(view: View): View {
        return super.beforeViewReturned(view)
            .also { it.setBackgroundColor(HexColors.YELLOW_600.colorInt) }
            .also { it.findViewById<ImageView>(R.id.imgv_matrix)?.background = createShapeDrawable(strokeWidthInDp = 1,borderColor = HexColors.BLUE_100.colorInt) }
    }
}