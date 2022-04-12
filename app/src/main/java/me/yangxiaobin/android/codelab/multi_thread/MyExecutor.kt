package me.yangxiaobin.android.codelab.multi_thread

import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.TimeUnit

class MyExecutor : AbstractExecutorService() {

    private var state: Int = 0
    private val maxCount = 1 shl 29


    override fun execute(command: Runnable?) {
        TODO("Not yet implemented")
    }

    override fun shutdown() {
        TODO("Not yet implemented")
    }

    override fun shutdownNow(): MutableList<Runnable> {
        TODO("Not yet implemented")
    }

    override fun isShutdown(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isTerminated(): Boolean {
        TODO("Not yet implemented")
    }

    override fun awaitTermination(timeout: Long, unit: TimeUnit?): Boolean {
        TODO("Not yet implemented")
    }
}
