package me.yangxiaobin.android.keyboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.yangxiaobin.android.keyboard.databinding.FragmentKeyboardHeightBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.showKeyboardWithDelay
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class KeyboardFragment : AbsViewBindingFragment<FragmentKeyboardHeightBinding>() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "KeyboardHeight"

    override fun getActualBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentKeyboardHeightBinding.inflate(inflater, container, false)

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        binding.edt.showKeyboardWithDelay()
    }
}
