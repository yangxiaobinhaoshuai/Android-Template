package me.yangxiaobin.android.codelab.jetpack_components

import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.yangxiaobin.android.codelab.common.ButtonsFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility

class MutableSharedFlowFragment : ButtonsFragment() {

    override val LogAbility.TAG: String get() = "MutableSharedFlow"

    private val defaultFlow = MutableSharedFlow<Int>()

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        defaultFlow.tryEmit(1)

        defaultFlow.subscriptionCount
            .onEach {
                logD("subscribe count: $it.")
            }.launchIn(lifecycleScope)


        defaultFlow
            .onEach {
                logD("""
                    collector1 :$it
                """.trimIndent())
            }
            .launchIn(lifecycleScope)

        lifecycleScope.launch {

            defaultFlow.collect{
                logD("""
                    collector2 :$it
                """.trimIndent())
            }

        }

        lifecycleScope.launch(Dispatchers.IO) {
            defaultFlow.emit(2)
        }

    }

    override fun onClick(index: Int) = when (index) {
        0 -> {}
        1 -> Unit
        2 -> Unit
        3 -> Unit
        4 -> Unit
        else -> Unit
    }
}

