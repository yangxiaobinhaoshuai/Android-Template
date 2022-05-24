package me.yangxiaobin.android.codelab.jetpack_components

import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.kotlin.codelab.ext.DelegateAdapter
import me.yangxiaobin.logger.clone
import me.yangxiaobin.logger.core.LogFacade
import me.yangxiaobin.logger.core.LogLevel

@Suppress("EXPERIMENTAL_API_USAGE_FUTURE_ERROR")
class MutableSharedFlowFragment : me.yangxiaobin.common_ui.ButtonsFragment() {

    override val logger: LogFacade get() = AndroidLogger.clone(logLevel = LogLevel.DEBUG, globalTagPrefix = Thread.currentThread().id.toString())

    override val LogAbility.TAG: String get() = "MutableSharedFlow"

    private val emptyBufferSuspendFlow = MutableSharedFlow<Int>()
    private var emptyBufferSuspendCounter  by DelegateAdapter.observableOnlyNew(0){
        logD("emptyBufferSuspendCounter changed: $it.")
    }

    private val oneBufferFlow = MutableSharedFlow<Int>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)
    private var oneBufferCounter by DelegateAdapter.observableOnlyNew(0){
        logD("oneBufferCounter changed: $it.")
    }


    private val replayFlow = MutableSharedFlow<Int>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)
    private var replayCounter by DelegateAdapter.observableOnlyNew(0){
        logD("replayCounter changed: $it.")
    }

    private val multiBufferFlow = MutableSharedFlow<Int>(extraBufferCapacity = 3, replay = 4, onBufferOverflow = BufferOverflow.SUSPEND)
    private var multiFlowCounter by DelegateAdapter.observableOnlyNew(0){
        logD("multiCounter, changed: $it.")
    }

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        //initEmptyBufferSuspendFlow()
        //initOneBufferFlow()
        //initReplayFlow()
        initMultiBufferFlow()
    }

    private fun initEmptyBufferSuspendFlow() {

        // 1.
        val r = emptyBufferSuspendFlow.tryEmit(++emptyBufferSuspendCounter)
        logD("Normal tryEmitRes :$r.")


        lifecycleScope.launch(Dispatchers.IO + CoroutineName("emptyBufferSuspendFlowCollector")) {
            delay(200)

            // 6.
            val tryEmitRes = emptyBufferSuspendFlow.tryEmit(++emptyBufferSuspendCounter)
            logD("lifecycleScope launch tryEmitRes :$tryEmitRes.")

            // 7.
            emptyBufferSuspendFlow.emit(++emptyBufferSuspendCounter)

            logD("sent lifecycleScope event, cur val: $emptyBufferSuspendCounter.")
        }

        // 2.
        emptyBufferSuspendFlow.tryEmit(++emptyBufferSuspendCounter).also { logD("after launch tryEmit: $it.") }


        emptyBufferSuspendFlow
            .onEach {
                // 4. -> 3
                // 8. -> 5
                logD("""
                    emptyBufferSuspendFlow collect :$it.
                """.trimIndent())
            }
            .launchIn(lifecycleScope)

        // 3.
        emptyBufferSuspendFlow.tryEmit(++emptyBufferSuspendCounter).also { logD("after collect tryEmit: $it.") }


        lifecycleScope.launch {
            // 5.
            emptyBufferSuspendFlow.emit(++emptyBufferSuspendCounter)
            logD("Last emit, cur val :$emptyBufferSuspendCounter.")
        }

    }

    private fun initOneBufferFlow() {

        oneBufferFlow.tryEmit(++oneBufferCounter).also { logD("first tryEmit, res: $it.") }

        oneBufferFlow
            .onEach {
                delay(500)
                logD("oneBuffer collect :$it.")
            }.launchIn(lifecycleScope)

        oneBufferFlow.tryEmit(++oneBufferCounter).also { logD("second tryEmit, res: $it.") }

        lifecycleScope.launch {

            oneBufferFlow.tryEmit(++oneBufferCounter).also { logD("third tryEmit, res: $it.") }
            oneBufferFlow.tryEmit(++oneBufferCounter).also { logD("third2 tryEmit, res: $it.") }
            oneBufferFlow.tryEmit(++oneBufferCounter).also { logD("third3 tryEmit, res: $it.") }

            delay(100)

            oneBufferFlow.emit(++oneBufferCounter).also { logD("forth emit.") }

        }

        oneBufferFlow.tryEmit(++oneBufferCounter).also { logD("fifth tryEmit, res: $it.") }
    }

    private fun initReplayFlow(){
        replayFlow.tryEmit(++replayCounter).also { logD("first tryEmit, res: $it.") }

        replayFlow.onEach {
            logD("replayFlow collect: $it.")
        }.launchIn(lifecycleScope)

        replayFlow.tryEmit(++replayCounter).also { logD("second tryEmit, res: $it.") }

        lifecycleScope.launch {
            replayFlow.tryEmit(++replayCounter).also { logD("third tryEmit, res: $it.") }

            replayFlow.emit(++replayCounter).also { logD("lifecycleScope emit.") }

        }

        replayFlow.tryEmit(++replayCounter).also { logD("forth tryEmit, res: $it.") }

    }

    private fun initMultiBufferFlow(){

        multiBufferFlow
            .onEach {
                logD("MultiFlow collect: $it.")
            }.launchIn(lifecycleScope)

        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("1st tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("2nd tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("3rd tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("4th tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("5th tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("6th tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("7th tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("8th tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("9th tryEmit, res: $it.") }

//        multiBufferFlow
//            .onEach {
//                logD("MultiFlow collect: $it.")
//            }.launchIn(lifecycleScope)


        lifecycleScope.launch {
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("11th tryEmit, res: $it.") }
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("12th tryEmit, res: $it.") }
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("13th tryEmit, res: $it.") }

            //delay(100)

            multiBufferFlow.emit(++multiFlowCounter).also { logD("14th emit.") }
            multiBufferFlow.emit(++multiFlowCounter).also { logD("15th emit.") }
            multiBufferFlow.emit(++multiFlowCounter).also { logD("16th emit.") }
            multiBufferFlow.emit(++multiFlowCounter).also { logD("17th emit.") }
            multiBufferFlow.emit(++multiFlowCounter).also { logD("18th emit.") }

        }


        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("19th tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("20th tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("21th tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("22th tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("23th tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("24th tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("25th tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("26th tryEmit, res: $it.") }
        multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("27th tryEmit, res: $it.") }


        lifecycleScope.launch {
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("28th tryEmit, res: $it.") }
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("29th tryEmit, res: $it.") }
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("30th tryEmit, res: $it.") }
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("31th tryEmit, res: $it.") }
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("32th tryEmit, res: $it.") }
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("33th tryEmit, res: $it.") }
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("34th tryEmit, res: $it.") }
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("35th tryEmit, res: $it.") }
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("36th tryEmit, res: $it.") }
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("37th tryEmit, res: $it.") }
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("38th tryEmit, res: $it.") }
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("39th tryEmit, res: $it.") }
            multiBufferFlow.tryEmit(++multiFlowCounter).also { logD("40th tryEmit, res: $it.") }
        }
    }


    override fun onClick(index: Int) {
        when (index) {

            // empty flow.
            0 -> {
                val res = emptyBufferSuspendFlow.tryEmit(++emptyBufferSuspendCounter)
                logD("sent click tryEmit event res :$res.")
            }
            1 -> lifecycleScope.launch {
                emptyBufferSuspendFlow.emit(++emptyBufferSuspendCounter)
                logD("sent emit event, cur val: $emptyBufferSuspendCounter.")
            }

            // one buffer flow.
            2 -> oneBufferFlow.tryEmit(++oneBufferCounter).also { logD("click tryEmit res:$it.") }
            3 -> lifecycleScope.launch {
                oneBufferFlow.emit(++oneBufferCounter)
                logD("click emit.")
            }

            // replayFlow
            4 -> replayFlow.tryEmit(++replayCounter).also { logD("click tryEmit res: $it.") }
            5-> lifecycleScope.launch {
                replayFlow.emit(++replayCounter)
                logD("click emit.")
            }
            else -> Unit
        }
    }
}

