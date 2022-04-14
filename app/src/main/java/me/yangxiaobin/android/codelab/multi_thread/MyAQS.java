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
 * 在独占模式中，该类定义了一个 Condition 对象来用作条件变量，通过 isHeldExclusively 来判断当前同步锁是否由当前 thread 独占， acquire 会为 state 赋值，release 方法会释放这个 condition,
 * condition 会恢复之前的 acquired state
 * 没有任何其他方法会创建 condition, 如果不满足语义，不要使用 condition.
 * Condition 的语义取决于 同步器的语义。
 *
 * 该类为 内部队列 和 condition 对象提供  审查、插桩、监视 方法； 这些方法可以开放给同步器。
 *
 * 该类的序列化只用于保存 state 的 int 状态, 反序列化会得到空 thread 队列，谨慎定义你序列化和反序列化逻辑。
 *
 * 使用：
 * 访问，审查，修改同步器状态通过，getState / setState / compareSetAndGet
 *
 * 1. tryAcquire
 * 2. tryRelease
 * 3. tryAcquireShared
 * 4. tryReleaseShared
 * 5. isHeldExclusively
 *
 *  1.这些方法默认都会抛异常;
 *  2.方法实现必须是线程安全的;
 *  3.必须短小高效，不能阻塞;
 *  4.重写这些方法是使用这个类的唯一语义, 其他方法被标记 final, 禁止修改
 *
 *  通过 AbstractOwnableSynchronizer 可以获取当前的 thread, 有利于监视和诊断持有锁的线程
 *
 *  尽管这类是基于 FIFO 队列实现的，但是并不强制遵循 FIFO acquire 语义，独占的同步逻辑如下：
 *
 *  Acquire:
 *  while( !tryAcquire(args) ) {
 *      enqueue thread if not already queued.
 *      possibly block current thread.
 *  }
 *
 *  Release:
 *  if( tryRelease(args) unlock the first queued thread.
 *
 *  共享模式逻辑类似，但是会设计到级联信号 （ cascading signals ）
 *
 *  在 acquire 里，入队之前现会先进行 check, 新竞争的 thread 可能会出现在队列最前面，如果你想，你可以禁止这种竞争，来提供公平锁。
 *  因此，大多数公平锁都会 hasQueuedPredecessors 返回 false, 非公平锁返回 true。
 *
 *  默认的非公平模式，吞吐能力和伸缩性是最高的， 但无法保证公平和线程饥饿，因为新线程可能比队列线程先获得锁，
 *  并且，因为 acquire 不会自旋，通常，会循环执行 tryAcquire 模拟自旋，在旋转时候可以通过 hasContended 和 hasQueuedThreads 进行选择判断，提高效率。
 *
 *  该类提供了高吞吐，高伸缩性通过使用 int 的 state，带参数的 acquire / release ，和 FIFO 的等待队列，如果不满足需求，通过 atomicInteger，queue 和 lockSupport 来自行实现逻辑。
 *
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
