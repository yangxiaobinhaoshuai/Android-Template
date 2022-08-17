package me.yangxiaobin.android.codelab.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * From : https://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-spacing
 */
class GridPaddingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean,
    log: (message: String) -> Unit
) : LogAwareItemDecoration(log) {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        // item position
        val position = parent.getChildAdapterPosition(view)

        // item column
        val column: Int = position % spanCount

        if (includeEdge) {
            // spacing - column * ((1f / spanCount) * spacing)
            outRect.left = spacing - column * spacing / spanCount
            // (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount
            // top edge
            if (position < spanCount) {
                outRect.top = spacing
            }
            // item bottom
            outRect.bottom = spacing
        } else {
            // column * ((1f / spanCount) * spacing)
            outRect.left = column * spacing / spanCount
            // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                // item top
                outRect.top = spacing
            }
        }
    }
}
