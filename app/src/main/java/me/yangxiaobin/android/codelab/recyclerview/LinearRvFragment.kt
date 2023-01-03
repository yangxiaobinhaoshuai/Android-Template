package me.yangxiaobin.android.codelab.recyclerview

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import me.yangxiaobin.android.codelab.R
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.setOnItemClickListener
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.setSimpleDivider
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.kotlin.codelab.recyclerview.AbsVH
import me.yangxiaobin.android.kotlin.codelab.recyclerview.SimpleRvAdapter
import me.yangxiaobin.logger.core.LogFacade

class LinearRvFragment : AbsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "LinearRv"

    override val layoutResId: Int = R.layout.fragment_recyclerview


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        val mockList = mutableListOf<Int>()
        repeat(30) { mockList += it }

        rv_fragment.layoutManager = LinearLayoutManager(requireContext())
        rv_fragment.adapter = SimpleRvAdapter<Int>(
            mockList,
            android.R.layout.simple_list_item_1
        ) { (vh: AbsVH,_, pos, _) ->
            vh.requireView<TextView>(android.R.id.text1).run {
                gravity = Gravity.CENTER
                text = mockList[pos].toString()
            }
        }

        rv_fragment.setSimpleDivider()

        rv_fragment.setOnItemClickListener(onLongClick = {
            logD("Linear onLongClick pos :${it.second}")
            false
        }, onClick = {
            logD("Linear onClick pos :${it.second}")
        })

    }

    override fun onBackPress() {
        super.onBackPress()
        logD("Linear backPress")
    }
}
