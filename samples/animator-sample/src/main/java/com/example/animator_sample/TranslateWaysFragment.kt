package com.example.animator_sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.animator_sample.databinding.FragmentTranslateWaysBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingFragment
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.androidColor
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.ui.kit.createRectAngleShapeDrawable
import me.yangxiaobin.logger.core.LogFacade

class TranslateWaysFragment : AbsViewBindingFragment<FragmentTranslateWaysBinding>() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "TranslateWaysFragment"

    private val tv by lazy { binding.tvTranslateWays }

    override fun getActualBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentTranslateWaysBinding =
        FragmentTranslateWaysBinding.inflate(inflater, container, false)


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        tv.background = createRectAngleShapeDrawable(
            solidColor = androidColor.LTGRAY,
            borderColor = androidColor.RED,
            strokeWidthInDp = 1
        )


        lifecycleScope.launch {
            delay(2000)
            tv.translationY = -200F
        }

    }


}
