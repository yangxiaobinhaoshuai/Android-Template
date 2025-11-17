package com.wkj.rv.lib.common

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wkj.rv.lib.SimpleRvAdapter

/**
 * Only for demo list of THIS project, not api.
 */
fun realSimpleRecyclerView(context: Context): RecyclerView {
    val rv = RecyclerView(context)

    rv.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

    rv.layoutManager = LinearLayoutManager(context)
    rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    val dataList = List(100) { it }


    val simpleRvAdapter = SimpleRvAdapter<Int>(
        dataList,
        android.R.layout.simple_list_item_1
    ) { (vh, entity, pos, payloads) ->
        val tv = vh.requireView<TextView>(android.R.id.text1)
        tv.text = "Item #$entity"

    }

    rv.adapter = simpleRvAdapter

    return rv

}


fun test() {

    val adapter = smartAdapter {
        register<String>(layoutId = 0) {
            areItemsSame { (old, new) -> old == new }
            areContentsSame { (old, new) -> old == new }
            // 如果内容变化只改名字，给一个 payload
            getChangePayload { (old, new) ->
                SmartPayload.NOOP
            }

            onClick { (item, position) ->

            }
            onLongClick { (item, position) ->
                true
            }
            onBind { item ->

            }
        }
    }
}

