package me.yangxiaobin.android.codelab.jetpack_components

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import me.yangxiaobin.android.kotlin.codelab.recyclerview.SimpleRvAdapter
import me.yangxiaobin.common_ui.EmptyFragment


class Paging3Fragment : EmptyFragment() {

    override fun customRootViewGroup(context: Context): ViewGroup {
        val rv = RecyclerView(requireContext())
        rv.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rv.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        val dataList = List(100) { it }
        rv.adapter = SimpleRvAdapter<Int>(
            dataList,
            android.R.layout.simple_list_item_1
        ) { (vh, entity, pos, payloads) ->
            val tv = vh.requireView<android.widget.TextView>(android.R.id.text1)
            tv.text = "Item #$entity"

        }
        return rv
    }


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
        view.setBackgroundColor(android.graphics.Color.WHITE)
        view.isClickable = true
    }


}