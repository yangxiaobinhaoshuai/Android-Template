package com.example.animator_sample

import com.example.animator_sample.databinding.ActivityAnimatorExampleBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingActivity

class AnimatorExampleActivity : AbsViewBindingActivity<ActivityAnimatorExampleBinding>() {

    override fun getActualBinding(): ActivityAnimatorExampleBinding =
        ActivityAnimatorExampleBinding.inflate(this.layoutInflater)
}