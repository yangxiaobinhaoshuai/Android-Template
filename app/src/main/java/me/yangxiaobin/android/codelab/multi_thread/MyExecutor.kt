package me.yangxiaobin.android.codelab.multi_thread

import java.util.concurrent.*
import java.util.concurrent.locks.AbstractQueuedSynchronizer
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

/**
 * 1.增强，提高效率
 * 2.限制，避免 OOM
 * 3.信息统计
 */
class MyExecutor : AbstractExecutorService() {

    /**
     * 0 running
     * 1 shutDown
     * 2 terminal
     */
    private var state: Int = 0

    private lateinit var blockQueue: BlockingQueue<Runnable>

    private val workers: MutableSet<Worker> = mutableSetOf()
    private var corePoolSize = 10
    private var maxPoolSize = 20

    private var rejectHandler: RejectHandler? = null

    /**
     * 添加、移除 worker thread
     * 获取活跃任务数量、已完成任务数量等
     */
    private val lock = ReentrantLock()

    private val termination: Condition = lock.newCondition()

    private val threadFactory: ThreadFactory = Executors.defaultThreadFactory()

    interface RejectHandler {
        fun reject(r: Runnable)
    }

    /**
     * 准备执行 runnable，可能使用新 thread， 也可能使用 pool thread
     *
     * 如果 runnable 提交失败，无论是是因为 executor shutDown 还是 thread 数量到达上限，都要执行 reject
     *
     * 1. 当前 thread count < core pool size ，直接 new 新线程执行
     * 2. executor 正在运行，且可以入队，添加 runnable 入队
     * 3. 超过最大数量， reject runnable
     *
     */
    override fun execute(command: Runnable) {

        if (workers.size < corePoolSize) {
            addWorkers(command, true)
            return
        } else if (!blockQueue.offer(command) && workers.size < maxPoolSize) {
            addWorkers(command, false)
        } else {
            if (!blockQueue.offer(command)) {
                rejectHandler?.reject(command)
            }
        }

    }

    /**
     * 根据当前 executor 状态和给定 bound ，决定是否可以成功添加 runnable
     *
     * executor 在 shutDown 时侯会返回 false，添加失败要回滚状态
     *
     * 注意多线程问题
     *
     * while true + cas
     */
    private fun addWorkers(r: Runnable?, useCoreBound: Boolean): Boolean {

        // 通过 while true + cas 来确保状态查询正确
        if (state > 0) return false

        val bound = if (useCoreBound) corePoolSize else maxPoolSize
        if (workers.size >= bound) return false

        if (blockQueue.offer(r)) return true

        lock.lock()

        return try {
            workers.add(Worker(r))
        } finally {
            lock.unlock()
        }
    }

    override fun shutdown() {
        interruptIdleWorkers()
    }

    private fun interruptIdleWorkers() {

        workers.forEach { w: Worker ->
            if (w.thread?.isInterrupted == false && w.tryLock()) {
                w.thread.interrupt()
            }
        }
    }

    private fun interruptWorkers() {
        workers.forEach { w: Worker ->
            w.thread?.interrupt()
        }
    }

    override fun shutdownNow(): MutableList<Runnable> {
        interruptWorkers()
        val tasks = mutableListOf<Runnable>()
        blockQueue.drainTo(tasks)
        return tasks
    }

    override fun isShutdown(): Boolean = state > 1

    override fun isTerminated(): Boolean = state > 2

    @Throws(InterruptedException::class)
    override fun awaitTermination(timeout: Long, unit: TimeUnit): Boolean {

        var nanos = unit.toNanos(timeout)

        lock.lock()
        try {
            while (state != 3) {
                if (nanos <= 0) return false
                nanos = termination.awaitNanos(nanos)
            }
            return true
        } finally {
            lock.unlock()
        }
    }

    /**
     * 1. 线程可能自带任务或者从 queue 中取任务，如果线程发生意外退出，应该用 try finally 启动一个新的 thread
     * 2. 线程开始运行之前是不会接收 interrupt
     * 3. 提供一个开始执行之前的钩子方法 beforeExecute
     * 4. 收集 run 方法中的 runtimeException， error， throwable 传递给 afterExecute
     * 5. 钩子方法 afterExecute
     */
    private fun runWorker(w: Worker) {

        var completeAbruptly = true
        w.unlock()
        var task = w.initialTask
        try {
            while (task != null || getTask().also { task = it } != null) {
                w.lock()

                // beforeExecute
                var th: Throwable? = null
                try {
                    try {
                        task?.run()
                    } catch (e: RuntimeException) {
                        th = e
                    } catch (e: Error) {
                        th = e
                    } catch (t: Throwable) {
                        th = t
                    } finally {
                        // afterExecute
                    }

                } finally {
                    w.unlock()
                }

            }
            completeAbruptly = true
        } finally {
            processWorkExit(w, completeAbruptly)
        }
    }

    private fun processWorkExit(w: Worker, isCompleteAbruptly: Boolean) {

        // 计算 worker 完成任务数量
        // 移除 worker
        // 尝试是否可以 terminate

        if (isCompleteAbruptly) {
            addWorkers(null, false)
        }
    }

    private fun getTask(): Runnable? {

        while (true) {

            if (state < 1) return null

            val task: Runnable? = try {
                blockQueue.take()
            } catch (e: Exception) {
                null
            }
            return task
        }
    }

    /**
     * 1. 标识 runnable 正在运行
     * 2. 防止重入 （setCorePoolSize), 正在工作的 task 禁止再次访问，如进行打断
     * 3. 未进入工作状态被 interrupt
     */
    inner class Worker(val initialTask: Runnable? = null) : Runnable,
        AbstractQueuedSynchronizer() {

        val thread: Thread?

        init {
            this.state = -1
            this.thread = threadFactory.newThread(initialTask)
        }

        override fun run() {
            runWorker(this)
        }

        fun lock() {
            acquire(1)
        }

        fun unlock() {
            release(1)
        }

        fun isLocked(): Boolean = isHeldExclusively

        fun tryLock(): Boolean = tryAcquire(1)

        override fun tryAcquire(arg: Int): Boolean {
            if (compareAndSetState(0, 1)) {
                exclusiveOwnerThread = Thread.currentThread()
                return true
            }
            return false
        }

        override fun tryRelease(arg: Int): Boolean {
            exclusiveOwnerThread = null
            state = 0
            return true
        }

        override fun isHeldExclusively(): Boolean = state != 0
    }
}
