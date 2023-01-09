package com.yxb.gesture.example

import com.example.gesture_example.databinding.ActivityScaleGestureBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingActivity
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class ScaleGestureActivity : AbsViewBindingActivity<ActivityScaleGestureBinding>() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "ScaleGesture@@"

    override fun getActualBinding(): ActivityScaleGestureBinding =
        ActivityScaleGestureBinding.inflate(this.layoutInflater)


    override fun afterOnCreate() {
        super.afterOnCreate()

    }

}