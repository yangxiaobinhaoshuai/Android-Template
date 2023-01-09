package com.example.animator_sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.animator_sample.databinding.ActivityAnimatorExampleBinding
import com.example.animator_sample.databinding.FragmentAnimatorExampleBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class AnimatorExampleFragment : AbsViewBindingFragment<FragmentAnimatorExampleBinding>() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "AnimatorExampleActivity@@"

    override fun getActualBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentAnimatorExampleBinding  = FragmentAnimatorExampleBinding.inflate(inflater,container,false)


}