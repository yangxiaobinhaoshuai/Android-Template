package me.yangxiaobin.android.codelab

import kotlinx.android.synthetic.main.activity_empty_main.*
import me.yangxiaobin.android.kotlin.codelab.base.AbsActivity

class EmptyActivity : AbsActivity() {


    override val contentResId: Int = R.layout.activity_empty_main


    override fun afterOnCreate() {
        super.afterOnCreate()
        init()
    }

    private fun init() {
        bt_empty_activity.setOnClickListener {

        }
    }

}
