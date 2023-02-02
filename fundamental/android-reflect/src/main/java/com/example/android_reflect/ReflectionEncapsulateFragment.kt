package com.example.android_reflect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.logger.core.LogFacade
import org.joor.Reflect

class ReflectionEncapsulateFragment : AbsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "ReflectionFragment"

    override fun createRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = createEmptyFrameLayout()
        .apply { this.setBackgroundColor(HexColors.GRAY_800.colorInt)  }

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
        testForJoor()
    }

    private fun testForJoor(){
        val str = ""
        Reflect.on(str)
    }


}