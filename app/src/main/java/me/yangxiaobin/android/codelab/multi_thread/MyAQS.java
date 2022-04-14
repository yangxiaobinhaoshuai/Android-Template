package me.yangxiaobin.android.codelab.multi_thread;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;


/**
 * 提供基于 FIFO 的等待队列实现 阻塞 和 同步器的 framework.
 * 该类是众多同步器的基础，使用 int 表示状态
 * 子类应该重写 protected 方法来改变状态，定义 state 被 acquire 和 release 时代表什么状态。
 * 鉴于此，该类的其他方法描述 '队列' 和 '阻塞' 逻辑，子类需要维护其他状态字段，但是只有被 setState / getState / compareSetAndGet 管理的状态会被同步
 *
 * 子类应该定义 non-public internal helper 来实现同步语义，AQS 并未实现任何同步接口，但确定义了诸如 acquireInterruptibly 可以被具体锁和同步器调用来实现各自逻辑
 *
 * 该类支持 独占 和 共享 两种模式； 独占模式，其他 thread acquire 不会成功，共享模式中，其他 Thread 可能会 acquire 成功;
 * 在共享模式中，除非一个 acquire 成功，下一个等待 thread 必须决定是否可以 acquire，否则该类并不知道区别；
 * 不同模式等待的 thread 共享同一个队列；子类可以选择实现一种模式或者两种模式；
 * 
 * 在独占模式中，该类定义了一个 Condition 对象，
 *
 */
public class MyAQS extends AbstractQueuedSynchronizer {

    protected MyAQS () {
        super();
    }

    @Override
    protected boolean tryAcquire (int arg) {
        return super.tryAcquire(arg);
    }

    @Override
    protected boolean tryRelease (int arg) {
        return super.tryRelease(arg);
    }

    @Override
    protected int tryAcquireShared (int arg) {
        return super.tryAcquireShared(arg);
    }

    @Override
    protected boolean tryReleaseShared (int arg) {
        return super.tryReleaseShared(arg);
    }

    @Override
    protected boolean isHeldExclusively () {
        return super.isHeldExclusively();
    }
}
