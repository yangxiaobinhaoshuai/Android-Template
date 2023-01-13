package com.example.animator_sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.animator_sample.databinding.FragmentAnimatorExampleBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingFragment
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.animator.Anim
import me.yangxiaobin.android.kotlin.codelab.ext.animator.getObjAnim
import me.yangxiaobin.android.kotlin.codelab.ext.animator.plus
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class AnimatorExampleFragment : AbsViewBindingFragment<FragmentAnimatorExampleBinding>() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "AnimatorExampleFragment@@"

    private val tv by lazy { binding.tvAnimatorActivity }
    private val bt1 by lazy { binding.bt1AnimFragment }
    private val bt2 by lazy { binding.bt2AnimFragment }

    override fun getActualBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentAnimatorExampleBinding =
        FragmentAnimatorExampleBinding.inflate(inflater, container, false)

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        bt1.setOnClickListener {

            val xScale = tv.getObjAnim(Anim.SCALE_X, 1F, 1.5F)
            val yScale = tv.getObjAnim(Anim.SCALE_Y, 1F, 1.5F)

            val yTrans = tv.getObjAnim(Anim.TRANSLATION_Y, -200F)

            (xScale + yScale + yTrans).start()

        }

        bt2.setOnClickListener {

            val x = tv.getObjAnim(Anim.SCALE_X, 1.5F, 1F)
            val y = tv.getObjAnim(Anim.SCALE_Y, 1.5F, 1F)

            val t = tv.getObjAnim(Anim.TRANSLATION_Y, 0F)

            (x + y + t).start()

        }
    }


}