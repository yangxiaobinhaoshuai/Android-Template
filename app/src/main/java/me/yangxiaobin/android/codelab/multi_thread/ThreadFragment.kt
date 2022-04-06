package me.yangxiaobin.android.codelab.multi_thread

import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import me.yangxiaobin.android.codelab.common.ButtonsFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.kotlin.codelab.ext.getFullStacktrace
import me.yangxiaobin.kotlin.codelab.ext.getLimitStacktrace
import me.yangxiaobin.logger.core.LogFacade
import java.util.concurrent.ThreadFactory

class ThreadFragment : ButtonsFragment() {

    override val LogAbility.TAG: String get() = "ThreadFragment"

    override val logger: LogFacade get() = AndroidLogger

    private val curThread get() = Thread.currentThread()

    private val thread = Thread()

    private val throwable = Throwable()


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        //initStacktrace()
        initInterrupt()
    }

    private fun initStacktrace() {
        logD("ThreadFragment dump :${curThread.stackTrace.joinToString(separator = "\r\n")}.")
        logD("\r\n------------")
        logD("ThreadFragment dump :${Throwable().stackTraceToString()}.")
        logD("\r\n------------")
        logD("cur trace :${getFullStacktrace()}")
        logD("\r\n------------")
        logD("cur trace :${getLimitStacktrace(4)}")
    }

    private fun initInterrupt() {

//        Thread.interrupted()
//        curThread.interrupt()

        lifecycleScope.launch {
            logD("cur interrupt :${curThread.isInterrupted}.")
            curThread.interrupt()
        }
    }
}

class MyThreadFactory : ThreadFactory {

    override fun newThread(r: Runnable): Thread {
        return Thread(r)
    }

}
