package me.yangxiaobin.android.codelab.jetpack_components

import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import me.yangxiaobin.android.codelab.common.ButtonsFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class FlowFragment : ButtonsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "FlowFragment"

    private val intFlow: Flow<Int> = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        .asFlow()
        .transform {
            delay(100)
            this.emit(it)
        }

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

//        lifecycleScope.launch {
//            intFlow.collectLatest {
//                logD(" int flow collect : $it.")
//            }
//        }
    }

    override fun onClick(index: Int) {
        super.onClick(index)
        when (index) {
            0 -> intFlow
                .onEach { logD("takeWhile before: $it.") }
                .takeWhile { it < 3 }
                .onEach { logD("takeWhile after: $it.") }
                .launchIn(lifecycleScope)
            1 -> intFlow
                .onEach { logD("transformWhile before: $it.") }
                .transformWhile {
                    this.emit(it)
                    this.emit(it)
//                    logD("it % 2 :${it % 2}.")
//                    it % 2 == 0
                    it < 3
                }
                .onEach { logD("transformWhile after: $it.") }
                .launchIn(lifecycleScope)
            2 -> intFlow
                //.onEach { logD("transform before: $it.") }
                .transform {
                    if (it % 2 == 0) this.emit(it)
                }
                .onEach { logD("transform after: $it.") }
                .launchIn(lifecycleScope)
            3 -> {}
            else -> {}
        }
    }
}
