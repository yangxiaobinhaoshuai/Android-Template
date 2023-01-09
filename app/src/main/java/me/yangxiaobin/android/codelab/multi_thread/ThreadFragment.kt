package me.yangxiaobin.android.codelab.multi_thread

import android.os.Message
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.createMainHandler
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.kotlin.codelab.ext.curThread
import me.yangxiaobin.kotlin.codelab.ext.getFullStacktrace
import me.yangxiaobin.kotlin.codelab.ext.getLimitStacktrace
import me.yangxiaobin.logger.core.LogFacade
import kotlin.concurrent.thread

class ThreadFragment : me.yangxiaobin.common_ui.ButtonsFragment() {

    override val LogAbility.TAG: String get() = "ThreadFragment"

    override val logger: LogFacade get() = AndroidLogger

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        //initStacktrace()
        initInterrupt()
    }

    //<editor-fold desc="get Current stacktrace">
    private fun initStacktrace() {
        logD("ThreadFragment dump :${curThread.stackTrace.joinToString(separator = "\r\n")}.")
        logD("\r\n------------")
        logD("ThreadFragment dump :${Throwable().stackTraceToString()}.")
        logD("\r\n------------")
        logD("cur trace :${getFullStacktrace()}")
        logD("\r\n------------")
        logD("cur trace :${getLimitStacktrace(4)}")
    }
    //</editor-fold>

    private fun initInterrupt() {
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
            2 -> twoThs()
            3 -> oneTh()
            4 -> optThreeThs()
            5 -> invokeCallback {
                logD("res :$it, callback invoke in cur :${curThread.name}.")
            }
            else -> Unit
        }
    }

    private fun oneTh() {
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

    private fun twoThs() {
        val runnable1 = Runnable {

            while (count < 50) {
                synchronized(lock) {
                    if (count % 2 == 1) {
                        lock.notify()
                        Thread.sleep(50)
                        // 单数
                        logD("t1: $count, thread:${Thread.currentThread().id}.")
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

    private fun threeThs() {

        val l1 = Object()
        val l2 = Object()
        val l3 = Object()

        val r1 = Runnable {
            while (count < 50) {
                synchronized(l1) {
                    synchronized(l2) {
                        //Thread.sleep(20)
                        l2.notify()
                        count += 1
                        logD("cur t :${Thread.currentThread().name}, count :$count.")
                    }
                    l1.wait()
                }
            }
        }

        val r2 = Runnable {
            while (count < 50) {
                synchronized(l2) {
                    synchronized(l3) {
                        //Thread.sleep(20)
                        l3.notify()
                        count += 1
                        logD("cur t :${Thread.currentThread().name}, count :$count.")
                    }
                    l2.wait()
                }
            }
        }

        val r3 = Runnable {
            while (count < 50) {
                synchronized(l3) {
                    synchronized(l1) {
                        //Thread.sleep(20)
                        l1.notify()
                        count += 1
                        logD("cur t :${Thread.currentThread().name}, count :$count.")
                    }
                    l3.wait()
                }
            }
        }

        Thread(r1, "t1").start()
        Thread(r2, "t2").start()
        Thread(r3, "t3").start()
    }

    private fun optThreeThs() {

        val a = Object()
        val b = Object()
        val c = Object()

        val r1 = MyRunnable(a, b)
        val r2 = MyRunnable(b, c)
        val r3 = MyRunnable(c, a)

        Thread(r1, "t1").start()
        Thread(r2, "t2").start()
        Thread(r3, "t3").start()
    }

    inner class MyRunnable(private val pre: Object, private val cur: Object) : Runnable {


        override fun run() {

            while (count < 50) {

                synchronized(pre) {
                    synchronized(cur) {
                        cur.notify()
                        count += 1
                        logD("cur t :${Thread.currentThread().name}, count :$count, state :${curThread.state}")
                    }
                    pre.wait()
                }

            }
        }

    }

    private fun dumpThread() = logD("cur interrupt :${curThread.isInterrupted}.")


    private val mainHandler = createMainHandler {
        logD("myHandler message: $it.")
        if (it.what == 1) callback?.invoke(it.obj as Int)
    }

    private var callback: ((Int) -> Unit)? = null


    private fun invokeCallback(callback: (Int) -> Unit) {
        this.callback = callback

        thread {
            logD("compute in ${curThread.name}.")
            Thread.sleep(500)
//            Looper.getMainLooper().thread.join()

            val message = Message.obtain()
            message.what = 1
            message.obj = 345
            mainHandler.sendMessage(message)
        }

    }
}
