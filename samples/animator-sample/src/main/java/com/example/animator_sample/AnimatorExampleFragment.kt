package com.example.animator_sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.animator_sample.databinding.ActivityAnimatorExampleBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class AnimatorExampleFragment : AbsViewBindingFragment<ActivityAnimatorExampleBinding>() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "AnimatorExampleActivity@@"

    override fun getActualBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ActivityAnimatorExampleBinding  = ActivityAnimatorExampleBinding.inflate(inflater,container,false)


}