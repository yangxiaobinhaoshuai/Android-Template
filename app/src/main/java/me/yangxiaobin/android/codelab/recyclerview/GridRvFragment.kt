package me.yangxiaobin.android.codelab.recyclerview

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import me.yangxiaobin.android.codelab.R
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.recyclerview.AbsVH
import me.yangxiaobin.android.kotlin.codelab.recyclerview.SimpleRvAdapter

class GridRvFragment : AbsFragment() {

    override val layoutResId: Int
        get() = R.layout.fragment_recyclerview


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        val mockList = mutableListOf<Int>()
        repeat(30) { mockList += it }

        rv_fragment.layoutManager = GridLayoutManager(requireContext(),4)
        rv_fragment.adapter = SimpleRvAdapter<Int>(
            mockList,
            android.R.layout.simple_list_item_1
        ) { (vh: AbsVH, pos, _) ->
            vh.requireView<TextView>(android.R.id.text1).run {
                gravity = Gravity.CENTER
                text = mockList[pos].toString()
            }

        }

        val helperCallback = object: ItemTouchHelper.Callback(){

            override fun isLongPressDragEnabled(): Boolean {
                return super.isLongPressDragEnabled()
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(UP or DOWN or START or END,0)
            }

            override fun onMove(
                rv: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val oldPos = viewHolder.adapterPosition
                val newPos = target.adapterPosition
                rv.adapter?.notifyItemMoved(oldPos,newPos)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
            }
        }

        ItemTouchHelper(helperCallback).attachToRecyclerView(rv_fragment)

    }
}
