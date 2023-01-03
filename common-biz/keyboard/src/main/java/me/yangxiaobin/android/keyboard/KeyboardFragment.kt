package me.yangxiaobin.android.keyboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.yangxiaobin.android.keyboard.databinding.FragmentKeyboardHeightBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.getKeyboardHeightFlow
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.showKeyboardWithDelay
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

        requireActivity()
            .getKeyboardHeightFlow()
            .onEach {
                logD("keyboard height flow: $it.")
            }
            .launchIn(lifecycleScope)
    }
}
