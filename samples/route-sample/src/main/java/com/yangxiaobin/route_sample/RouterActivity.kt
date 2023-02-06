package com.yangxiaobin.route_sample

import android.view.View
import androidx.fragment.app.commit
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyActivity
import me.yangxiaobin.logger.core.LogFacade

class RouterActivity : EmptyActivity() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "RouterActivity"

    override fun afterOnCreate() {
        super.afterOnCreate()

    }

    override fun onButtonClick(v: View) {
        super.onButtonClick(v)

       /* val routerFragment = RouterFragment()
        this.supportFragmentManager.commit {
            this.add(android.R.id.content, routerFragment)
        }*/
    }

}