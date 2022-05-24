package me.yangxiaobin.android.codelab.multi_thread

import android.view.View
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.now
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.kotlin.codelab.ext.curThread
import me.yangxiaobin.logger.core.LogFacade
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

class ReentrantLockFragment : me.yangxiaobin.common_ui.ButtonsFragment() {

    override val LogAbility.TAG: String get() = "ReentrantLock"

    override val logger: LogFacade get() = AndroidLogger

    private val lock = ReentrantLock()
    private val condition = lock.newCondition()
    private var count = 0

     private val interruptRunnable = Runnable {
         try {
             lock.lockInterruptibly()
             logD("cur : ${curThread.name} locked, now :$now.")
         } catch (e: Exception) {
             e.printStackTrace()
             logD("cur :${curThread.name}, interruptRunnable catch :${e.stackTraceToString()}.")
         }
     }


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        Thread(interruptRunnable,"interrupt1").start()
        Thread.sleep(200)
        val t2 = Thread(interruptRunnable, "interrupt2")
        t2.start()
        t2.interrupt()
    }

    override fun onClick(index: Int) {
        super.onClick(index)
        count = 0
        when (index) {
            0 -> twoThs()
            1 -> threeThs()
            2 -> nThs(3)
            3 -> Unit
            4 -> Unit
            5 -> Unit
            else -> Unit
        }
    }

    private fun twoThs() {

        val r = Runnable {
            while (count < 50) {
                lock.lock()
                try {
                    condition.signal()
                    count += 1
                    logD("count :$count, ${curThread.id}, ${curThread.name}.")
                    condition.await()
                } finally {
                    lock.unlock()
                }
            }
        }

        Thread(r, "t1").start()
        Thread(r, "t2").start()
    }

    private fun threeThs() {

        val c1 = lock.newCondition()
        val c2 = lock.newCondition()
        val c3 = lock.newCondition()

        val r1 = Runnable {
            while (count < 50) {
                lock.lock()
                try {
                    c2.signal()
                    count += 1
                    logD("r1, count:$count, t name :${curThread.name}.")
                    c1.await()
                } finally {
                    lock.unlock()
                }
            }

        }
        val r2 = Runnable {
            while (count < 50) {
                lock.lock()
                try {
                    c3.signal()
                    count += 1
                    logD("r1, count:$count, t name :${curThread.name}.")
                    c2.await()
                } finally {
                    lock.unlock()
                }
            }
        }
        val r3 = Runnable {
            while (count < 50) {
                lock.lock()
                try {
                    c1.signal()
                    count += 1
                    logD("r1, count:$count, t name :${curThread.name}.")
                    c3.await()
                } finally {
                    lock.unlock()
                }
            }
        }

        Thread(r1, "t1").start()
        Thread(r2, "t2").start()
        Thread(r3, "t3").start()
    }

    private fun nThs(n: Int) {
        val c1 = lock.newCondition()
        val c2 = lock.newCondition()
        val c3 = lock.newCondition()

        val r1 = MyRunnable(c1, c2)
        val r2 = MyRunnable(c2, c3)
        val r3 = MyRunnable(c3, c1)

        Thread(r1, "t1").start()
        Thread(r2, "t2").start()
        Thread(r3, "t3").start()
    }

    inner class MyRunnable(private val pre: Condition, private val cur: Condition) : Runnable {

        override fun run() {

            while (count < 50) {
                lock.lock()
                try {
                    cur.signal()
                    count += 1
                    logD("count :$count, t name: ${curThread.name}.")
                    pre.await()
                } finally {
                    lock.unlock()
                }
            }
        }

    }

}
