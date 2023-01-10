package com.example.animator_sample

import android.animation.AnimatorSet
import androidx.core.os.postDelayed
import com.example.animator_sample.databinding.ActivityAnimatorExampleBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingActivity
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.animator.Anim
import me.yangxiaobin.android.kotlin.codelab.ext.animator.getObjAnim
import me.yangxiaobin.android.kotlin.codelab.ext.mainHandler
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class AnimatorExampleActivity : AbsViewBindingActivity<ActivityAnimatorExampleBinding>() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "AnimatorExampleActivity@@"

    override fun getActualBinding(): ActivityAnimatorExampleBinding =
        ActivityAnimatorExampleBinding.inflate(this.layoutInflater)

    override fun afterOnCreate() {
        super.afterOnCreate()

        val xAnim = binding.tvAnimatorActivity.getObjAnim(animEnum = Anim.ScaleX, 1F, 1.5F)
        val yAnim = binding.tvAnimatorActivity.getObjAnim(animEnum = Anim.ScaleY, 1F, 1.5F)

        val set = AnimatorSet()
        set.playTogether(xAnim, yAnim)

        mainHandler.postDelayed(1000) {
            set.start()
        }

        binding.btAnimActivity.setOnClickListener {
            set.cancel()

            val x = binding.tvAnimatorActivity.getObjAnim(animEnum = Anim.ScaleX, 1.5F, 1F)
            val y = binding.tvAnimatorActivity.getObjAnim(animEnum = Anim.ScaleY, 1.5F, 1F)


            val s = AnimatorSet()
            s.duration = 0
            s.playTogether(x, y)
            s.start()
        }
    }
}