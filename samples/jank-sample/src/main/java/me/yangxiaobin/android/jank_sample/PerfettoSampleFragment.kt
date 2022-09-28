package me.yangxiaobin.android.jank_sample

import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.mainHandler
import me.yangxiaobin.android.kotlin.codelab.ext.makeCancellable
import me.yangxiaobin.android.kotlin.codelab.ext.postInterval
import me.yangxiaobin.android.kotlin.codelab.ext.workerHandler
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyFragment
import me.yangxiaobin.logger.core.LogFacade

import androidx.tracing.trace as androidXTracing

/**
 * Systrace 下载 ： https://github.com/catapult-project/catapult/tree/master
 *
 * Perfetto ：https://ui.perfetto.dev/
 */
class PerfettoSampleFragment : EmptyFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "Perfetto-Sample"


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

//        timer(name = "jank timer", daemon = false, initialDelay = 1000L, period = 1000L) {
//            logD("timer executed: ${Thread.currentThread().name}.")
//            lgd()
//        }

//        workerHandler
//            .makeCancellable(this)
//            .postInterval(1000) {
//                lgd()
//            }

        flow {
            while (true) {
                delay(1000)
                emit(Unit)
            }
        }.onEach { lgd() }.launchIn(lifecycleScope)

    }


    private fun lgd() {
        androidXTracing("lgd") {
            logD("Thread sleep begin ->")
            Thread.sleep(500)
            logD("Thread sleep stop <-")
        }
    }

}
