
FAQ
1. 如何解决底层 lib 需要依赖 上层 lib 对的问题？
> 1 @Invoke & @ForInvoke 
> 2 如果底层需要顶层返回值，底层声明一个空 List<T>, 上层在编译时填充这个 List


IOC
1. What is IOC ?
2. Which problem does it solve ?
3. When it is appropriate or not ?
4. Why called DI as IOC ?

> StackOverflow ans : https://stackoverflow.com/a/3108/10247834
> 顾名思义，控制权 / 控制流的转换，就是 Inverse of control, 典型案例就是 GUI
> 把产生依赖的 控制权 转移 到的其他的 外部系统（IOC Container) 当中
> > 卖房带车库， 和买房自己有车库
