package me.yangxiaobin.android.codelab.multi_thread

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import me.yangxiaobin.android.codelab.common.ButtonsFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.kotlin.codelab.ext.curThread
import me.yangxiaobin.logger.core.LogFacade
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit

class FutureFragment : ButtonsFragment() {

    override val LogAbility.TAG: String get() = "FutureFragment"

    override val logger: LogFacade get() = AndroidLogger

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(index: Int) {
        super.onClick(index)
        when (index) {
            0 -> invokeFuture()
            1 -> invokeCompleteFuture()
            2 -> Unit
            3 -> Unit
            4 -> Unit
            5 -> Unit
            else -> Unit
        }
    }

    private fun invokeFuture() {

        val callable = Callable<Int> {
            logD("callable start, cur :${curThread.name}.")
            Thread.sleep(800)
            123
        }
        val f = FutureTask<Int>(callable)
        Thread(f).start()
        try {
            val res = f.get(2, TimeUnit.SECONDS)
            logD("future task res :$res.")
        } catch (e: Exception) {
            e.printStackTrace()
            logD("time out for get.")
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun invokeCompleteFuture() {

        val c1 = CompletableFuture.supplyAsync {
            logD("CompletableFuture.supplyAsync cur t : ${curThread.name}.")
            Thread.sleep(2000)
            40
        }
        val c2 = CompletableFuture.completedFuture(3)

        val c3 = CompletableFuture<Int>()

        c3.handleAsync { t: Int, u: Throwable ->
            logD("handleAsync, ${curThread.name}.")
            Thread.sleep(1500)
        }

        c3.thenApply {
            logD("thenApply, ${curThread.name}.")
            Thread.sleep(2500)
            it + 10
        }
        c3.whenComplete { t, u ->
            logD("c3 completed , $t.")
        }
        c3.complete(5)

        val res = c1.get()
        logD("Complete future res :$res.")
    }
}
