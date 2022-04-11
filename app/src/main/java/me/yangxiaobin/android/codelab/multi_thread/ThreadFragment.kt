package me.yangxiaobin.android.codelab.multi_thread

import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
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
            dumpThread()
            delay(200)
            curThread.interrupt()
            dumpThread()
        }
    }

    private var count = 0
    private val lock = Object()

    override fun onClick(index: Int) {
        super.onClick(index)
        when (index) {
            0 -> {
                Thread.interrupted()
                dumpThread()
            }
            1 -> {
                curThread.interrupt()
                dumpThread()
            }
            2 -> {
                try {
                    curThread.interrupt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    logD("catch the interrupt exception, ${e.printStackTrace()}.")
                }
                dumpThread()
            }
            3 -> {

                val runnable1 = Runnable {

                    while (count < 50) {
                        synchronized(lock) {
                            if (count % 2 == 1) {
                                lock.notify()
                                Thread.sleep(50)
                                // 单数
                                logD("t1: $count, thread:${Thread.currentThread().id}, ${Thread.currentThread().state}.")
                                count += 1
                                lock.wait()
                            }
                        }
                    }
                }

                val runnable2 = Runnable {

                    while (count < 50) {
                        synchronized(lock) {
                            if (count % 2 == 0) {
                                lock.notify()
                                Thread.sleep(50)
                                // 双数
                                logD("t2: $count, thread:${Thread.currentThread().id}, ${Thread.currentThread().state}.")
                                count += 1
                                lock.wait()
                            }
                        }
                    }
                }

                Thread(runnable1, "t1").start()
                Thread(runnable2, "t2").start()

            }
            4 -> {

                val runnable = Runnable {

                    while (count < 50) {
                        synchronized(lock) {
                            lock.notify()
                            Thread.sleep(20)
                            logD("cur t :${Thread.currentThread().name}, count :$count.")
                            count += 1
                            lock.wait()
                        }
                    }
                }

                Thread(runnable, "t1").start()
                Thread(runnable, "t2").start()
            }
            else -> Unit
        }
    }

    private fun dumpThread() {
        logD("cur interrupt :${curThread.isInterrupted}.")
    }
}

class MyThreadFactory : ThreadFactory {

    override fun newThread(r: Runnable): Thread {
        return Thread(r)
    }

}
