package me.yangxiaobin.android.codelab.multi_thread;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;

/**
 * 提供基于 FIFO 的等待队列实现 阻塞 和 同步器的 framework.
 * 该类是众多同步器的基础，使用 int 表示状态
 * 子类应该重写 protected 方法来改变状态，定义 state 被 acquire 和 release 时代表什么状态。
 * 鉴于此，该类的其他方法描述 '队列' 和 '阻塞' 逻辑，子类需要维护其他状态字段，但是只有被 setState / getState / compareSetAndGet 管理的状态会被同步
 * <p>
 * 子类应该定义 non-public internal helper 来实现同步语义，AQS 并未实现任何同步接口，但确定义了诸如 acquireInterruptibly 可以被具体锁和同步器调用来实现各自逻辑
 * <p>
 * 该类支持 独占 和 共享 两种模式； 独占模式，其他 thread acquire 不会成功，共享模式中，其他 Thread 可能会 acquire 成功;
 * 在共享模式中，除非一个 acquire 成功，下一个等待 thread 必须决定是否可以 acquire，否则该类并不知道区别；
 * 不同模式等待的 thread 共享同一个队列；子类可以选择实现一种模式或者两种模式；
 * <p>
 * 在独占模式中，该类定义了一个 Condition 对象来用作条件变量，通过 isHeldExclusively 来判断当前同步锁是否由当前 thread 独占， acquire 会为 state 赋值，release 方法会释放这个 condition,
 * condition 会恢复之前的 acquired state
 * 没有任何其他方法会创建 condition, 如果不满足语义，不要使用 condition.
 * Condition 的语义取决于 同步器的语义。
 * <p>
 * 该类为 内部队列 和 condition 对象提供  审查、插桩、监视 方法； 这些方法可以开放给同步器。
 * <p>
 * 该类的序列化只用于保存 state 的 int 状态, 反序列化会得到空 thread 队列，谨慎定义你序列化和反序列化逻辑。
 * <p>
 * 使用：
 * 访问，审查，修改同步器状态通过，getState / setState / compareSetAndGet
 * <p>
 * 1. tryAcquire
 * 2. tryRelease
 * 3. tryAcquireShared
 * 4. tryReleaseShared
 * 5. isHeldExclusively
 * <p>
 * 1.这些方法默认都会抛异常;
 * 2.方法实现必须是线程安全的;
 * 3.必须短小高效，不能阻塞;
 * 4.重写这些方法是使用这个类的唯一语义, 其他方法被标记 final, 禁止修改
 * <p>
 * 通过 AbstractOwnableSynchronizer 可以获取当前的 thread, 有利于监视和诊断持有锁的线程
 * <p>
 * 尽管这类是基于 FIFO 队列实现的，但是并不强制遵循 FIFO acquire 语义，独占的同步逻辑如下：
 * <p>
 * Acquire:
 * while( !tryAcquire(args) ) {
 * enqueue thread if not already queued.
 * possibly block current thread.
 * }
 * <p>
 * Release:
 * if( tryRelease(args) unlock the first queued thread.
 * <p>
 * 共享模式逻辑类似，但是会设计到级联信号 （ cascading signals ）
 * <p>
 * 在 acquire 里，入队之前现会先进行 check, 新竞争的 thread 可能会出现在队列最前面，如果你想，你可以禁止这种竞争，来提供公平锁。
 * 因此，大多数公平锁都会 hasQueuedPredecessors 返回 false, 非公平锁返回 true。
 * <p>
 * 默认的非公平模式，吞吐能力和伸缩性是最高的， 但无法保证公平和线程饥饿，因为新线程可能比队列线程先获得锁，
 * 并且，因为 acquire 不会自旋，通常，会循环执行 tryAcquire 模拟自旋，在旋转时候可以通过 hasContended 和 hasQueuedThreads 进行选择判断，提高效率。
 * <p>
 * 该类提供了高吞吐，高伸缩性通过使用 int 的 state，带参数的 acquire / release ，和 FIFO 的等待队列，如果不满足需求，通过 atomicInteger，queue 和 lockSupport 来自行实现逻辑。
 * <p>
 * <p>
 * 美团 AQS ： https://tech.meituan.com/2019/12/05/aqs-theory-and-apply.html
 * <p>
 * Doug lea 论文中译 （并发编程网） ： http://ifeve.com/aqs/  （博客园： https://www.cnblogs.com/dennyzhangdd/p/7218510.html）
 * <p>
 * 论文纪要：
 * 模板 + 委托设计模式
 * acquire / release
 * 1. 阻塞 / 非阻塞版本
 * 2. 超时中断
 * 3. 主动中断取消
 * <p>
 * 独占锁提供了条件变量实现，条件变量天生应该与独占锁实现的语义相关
 * <p>
 * 没有框架可以决定代表用户决定选择哪一种方式，应该提供不同的策略
 * 无论实现多么精雕细琢，也会在实践中产生瓶颈，框架应该提供监视工具,让用户发现和缓和瓶颈
 * <p>
 * acquire 语义： while(不允许获取锁） 排队，可能阻塞
 * release 语义： if（允许一个阻塞的线程获取锁）唤醒一个或者多个线程
 * <p>
 * 三个基本条件相互协作：
 * 1. 同步状态的原子性管理
 * 2. 线程的阻塞与解除阻塞
 * 3. 队列的管理
 * <p>
 * 设计考量：
 * 并未提供一个管理三个组件的方式，而是为三个组件选择一个具体实现，并在方法上有大量选项可用，有意的限制了限制了适用范围，提供了足够的效率。
 * <p>
 * 1. 同步状态
 * int + 原子操作
 * <p>
 * tryAcquire 和 tryRelease 要和 acquire / release 语义对应上。这些方法的参数配合 int state，增加了拓展能力
 * <p>
 * <p>
 * 2. 阻塞
 * LockSupport.unpark 没有计数，可以提前于 park 多次调用（只会起到一次效果），后续调用 park 会立即返回。
 * park 可以设置超时时间
 * park 可以被 interrupt 打断 unpark 一个线程
 * <p>
 * 3. 队列
 * 整个框架的关键就是如何管理被阻塞的线程队列，该队列是严格的 FIFO 模式，并不支持优先级同步。
 * 同步队列的最佳最佳实践是不使用底层锁。 （lockFree） 对此，业内很少有争议。
 * mcs 和 clh 做选择
 * clh 更容易实现 "超时" 和 "取消" 功能，最总设计与 CLH 出入较大
 * <p>
 * 包含 head 和 tail ，初始两者相等，通过 cas 不断修改队尾，出队只需要重新移动 head
 * <p>
 * CLH 优势在于出队，入队快速、无锁，探测是否有线程竞争也很快（只需判断 head == tail），状态分散于各个 node 各自维护，避免内存竞争
 * <p>
 * pre 变量可以处理'超时'和'取消'，自旋中节点连接不是必须的，因为当前节点的变化会被下一个节点观察到，但是阻塞模式中需要显示的唤醒下一个节点 locksupport.unpark(thread)
 * next 指针可以当作一个优化，因为赋值没有被原子保护，只是简单赋值，如果发现 next == null （看似被取消了）实则可以通过 tail -> pre 来遍历是否真的有后续节点。
 * <p>
 * 第二个区别于 clh 是节点状态用于控制阻塞而非自旋
 * <p>
 * 每个 acquire 和 release 都是 O1 操作。支持取消则是 On 复杂度。
 * <p>
 * 条件队列：
 * signal 只需把 node 从条件队列中转移到阻塞队列中。
 * <p>
 * <p>
 * 公平调度的控制：
 * 前一个锁释放锁，排队的下一个锁处于解除状态中，就会出现 gap, 非公平锁可以减少这种 gap ，提高吞吐率。
 */
public class MyAQS {

    static class Node {
        static final Node SHARED = new Node();
        static final Node EXCLUSIVE = new Node();
        static final int CANCEL = 1;
        static final int SIGNAL = -1;
        static final int CONDITION = -2;
        static final int PROPAGATE = -3;

        volatile int waitStatus;
        volatile Node pre;
        volatile Node next;
        volatile Thread thread;
        // Condition queue.
        Node nextWaiter;

        public Node () {
        }

        public Node (Node nextWaiter) {
            this.nextWaiter = nextWaiter;
            this.thread = Thread.currentThread();
        }

        public Node predecessor () {
            Node p = pre;
            if (p != null) {
                return p;
            } else {
                throw new NullPointerException();
            }
        }
    }

    private transient volatile Node head;
    private transient volatile Node tail;
    private volatile int state;

    public final int getState () {
        return state;
    }

    public final void setState (int state) {
        this.state = state;
    }

    public final boolean compareAndSetState (Node expect, Node update) {
        return false;
    }

    /**
     * 用 update 更换 except
     */
    private boolean compareAndSetTail (Node except, Node update) {
        return false;
    }

    private Node enq (Node node) {
        for (; ; ) {
            Node oldTail = this.tail;
            if (oldTail != null) {
                // cas
                node.pre = oldTail;
                // case
                if (compareAndSetTail(oldTail, node)) {
                    oldTail.next = node;
                    return oldTail;
                }
            } else {
                initSyncQueue();
            }
        }

    }

    private Node addWaiter (Node mode) {
        Node node = new Node(mode);

        for (; ; ) {
            Node oldTail = tail;
            if (oldTail != null) {
                // cas
                node.pre = oldTail;
                if (compareAndSetTail(oldTail, node)) {
                    oldTail.next = node;
                    return node;
                }

            } else {
                initSyncQueue();
            }

        }
    }

    /**
     * Dequeue node
     */
    private void setHead (Node node) {
        head = node;
        node.thread = null;
        node.pre = null;
    }


    private void initSyncQueue () {
        // cas set.
        head = tail = new Node();
    }

    /**
     * 独占模式请求获取锁,忽略中断
     *
     * @param arg
     */
    protected final void acquire (int arg) {
        if (tryAcquire(arg) && acquireQueued(addWaiter(new Node(Node.EXCLUSIVE)), arg)) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 是否能够入队成功，否则阻塞
     */
    protected boolean acquireQueued (Node node, int arg) {
        try {
            boolean interrupted = false;
            for (; ; ) {
                Node pre = node.predecessor();
                // 当前为队列中第一个元素，或者能够竞争锁成功
                if (pre == head || tryAcquire(arg)) {
                    setHead(node);
                    node.pre = null; // help gc
                    return interrupted;
                }

                if (shouldParkAfterAcquireFailed(pre, node) && parkAndCheckInterrupt()) {
                    interrupted = true;
                }
            }

        } catch (Throwable e) {
            cancelAcquire(node);
            throw e;
        }
    }

    /**
     * 在获取锁失败后是否需要阻塞
     *
     * @return
     */
    private boolean shouldParkAfterAcquireFailed (Node pre, Node cur) {
        int ws = pre.waitStatus;

        if (ws == Node.SIGNAL) {
            return true;
        } else if (ws > 0) {
            // 存在被 cancel 的 node, 移除被 cancel 的 node。
            do {
                cur.pre = pre = pre.pre;
            } while (pre.waitStatus > 0);

            pre.next = cur;

        } else {
            // cas
            pre.waitStatus = Node.SIGNAL;
        }
        return false;
    }

    /**
     * 阻塞当前 thread， 接收中断
     *
     * @return
     */
    private boolean parkAndCheckInterrupt () {
        LockSupport.park(this);
        return Thread.interrupted();
    }


    /**
     * 唤醒后继节点
     */
    private void unparkSuccessor (Node node) {

        int ws = node.waitStatus;

        // cas
        if (ws <= 0) ws = 0;

        Node next = node.next;

        if (next == null || next.waitStatus > 0) {
            for (Node p = tail; p != node && p != null; p = p.next) {

                if (p.waitStatus <= 0) {
                    next = p;
                }
            }
        }

        if (next != null) LockSupport.unpark(next.thread);
    }


    /**
     * 在阻塞队列中移除自己
     *
     * @param node
     */
    private void cancelAcquire (Node node) {

        if (node == null) return;

        node.thread = null;

        Node pre = node.pre;
        while (pre.waitStatus > 0) node.pre = pre = pre.pre;

        Node preNext = pre.next;

        node.waitStatus = Node.CANCEL;

        // 当前节点就是尾节点，移除自己
        if (node == tail && compareAndSetTail(node, pre)) {
            // case
            pre.next = null;
        } else {
            // 1. 如果队列后面元素需要被唤醒就重新设置有效的 pre
            // 2. 否则就唤醒 node

            int ws;
            // 非队列首节点，处于队列中间
            if (pre != head
                    //  cas
                    //  前一个节点也在阻塞
                    && ((ws = pre.waitStatus) == Node.SIGNAL || (ws <= 0 && pre.waitStatus == Node.SIGNAL)) && pre.thread != null) {

                Node next = node.next;

                if (next != null && next.waitStatus <= 0) {
                    preNext = next;
                }
            } else {
                // 队列头
                unparkSuccessor(node);
            }
        }

        node.next = null;

    }

    /**
     * 释放锁，唤醒阻塞队列第一个
     */
    public boolean release (int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0) {
                unparkSuccessor(h);
            }
            return true;
        }
        return false;
    }

    /**
     * 获取共享锁，忽略中断
     *
     * @param arg
     */
    public void acquireShared (int arg) {
        if (tryAcquireShared(arg) < 0) {
            doAcquireShared(arg);
        }
    }

    // TODO
    private void doAcquireShared (int arg) {
        Node node = addWaiter(new Node(Node.SHARED));
        try {
            boolean interrupted = false;
            for (; ; ) {
                Node p = node.predecessor();

                // 如果处于阻塞队列头部
                if (p == head) {
                    int res = tryAcquireShared(arg);
                    if (res > 0) {
                        p.next = null; // help GC
                        if (interrupted) Thread.currentThread().interrupt();
                        return;

                    }
                }

                if (shouldParkAfterAcquireFailed(p, node) && parkAndCheckInterrupt()) {
                    interrupted = true;
                }

            }

        } catch (Throwable throwable) {
            cancelAcquire(node);
            throw throwable;
        }
    }

    // TODO
    private void setHeadAndPropagate (Node node, int propagate) {

    }

    protected boolean tryAcquire (int arg) {
        return false;
    }

    protected boolean tryRelease (int arg) {
        return false;
    }

    /**
     * @param arg
     * @return < 0 表示异常； = 0 表示该次成功，但是后续 share 请求不会成功； >0 表示该次成功，后续 shared 请求也可能成功；
     */
    protected int tryAcquireShared (int arg) {
        return 0;
    }

    protected boolean tryReleaseShared (int arg) {
        return false;
    }

    protected boolean isHeldExclusively () {
        return false;
    }

    public boolean hasQueuedThreads () {
        return head != tail;
    }

    public boolean hasContended () {
        return head != null;
    }

    class ConditionObject implements Condition {

        @Override
        public void await () throws InterruptedException {

        }

        @Override
        public void awaitUninterruptibly () {

        }

        @Override
        public long awaitNanos (long nanosTimeout) throws InterruptedException {
            return 0;
        }

        @Override
        public boolean await (long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public boolean awaitUntil (Date deadline) throws InterruptedException {
            return false;
        }

        @Override
        public void signal () {

        }

        @Override
        public void signalAll () {

        }
    }
}
