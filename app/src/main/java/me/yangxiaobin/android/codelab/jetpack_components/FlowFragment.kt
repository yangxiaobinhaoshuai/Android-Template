package me.yangxiaobin.android.codelab.jetpack_components

import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class FlowFragment : me.yangxiaobin.common_ui.ButtonsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "FlowFragment"

    private val intFlow: Flow<Int> = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        .asFlow()
        .transform {
            delay(100)
            this.emit(it)
        }

    private val mutableIntFlow = MutableSharedFlow<Int>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        mutableIntFlow
            .distinctUntilChanged()
            .onEach {
                logD("mutableIntFlow value :$it.")
            }
            .launchIn(lifecycleScope)
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
            3 -> mutableIntFlow.tryEmit(9)
            4 -> {

                val flow1: Flow<Int> = flow {
                    emit(1)
                    delay(100)
                    emit(2)
                    delay(100)
                    emit(3)
                    delay(100)
                }

                val flow2: Flow<String> = flow {
//                    emit("A")
//                    delay(200)
//                    emit("B")
//                    delay(200)
                }

                val mutableFlow = MutableStateFlow("A")

                val combinedFlow: Flow<Pair<Int, String>> = flow1.combine(mutableFlow) { number, string ->
                    number to string
                }

                val zippedFlow: Flow<Pair<Int, String>> = flow1.zip(flow2) { number, string ->
                    number to string
                }

               val flatMapConcatedFlow = flow1.flatMapConcat { i->
                    flow2
                }

                lifecycleScope.launch {

                    combinedFlow.collect { pair ->
                        val number = pair.first
                        val string = pair.second
                        logD("====> Number: $number, String: $string")
                    }

//                    zippedFlow.collect { pair ->
//                        val number = pair.first
//                        val string = pair.second
//                        logD("====> Number: $number, String: $string")
//                    }

//                    flatMapConcatedFlow.collect{
//                        logD("====> flatMapConcatedFlow: $it")
//                    }


                }

            }
            5 -> {}
            else -> {}
        }
    }
}
