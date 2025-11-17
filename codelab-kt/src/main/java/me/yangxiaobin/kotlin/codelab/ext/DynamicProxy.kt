import java.lang.reflect.Method
import java.lang.reflect.Proxy


typealias InvocationParam = Pair<Method, Array<Any?>?>
typealias InvocationHook = (InvocationParam) -> Any?

operator fun InvocationHook.plus(other: InvocationHook): InvocationHook = { param ->
    // 先执行当前 hook（一般用于打 log、埋点等）
    this(param)
    // 再执行下一个 hook，返回它的结果
    other(param)
}


fun defaultHook(delegate: Any): InvocationHook = { (method, args) ->
    when (method.name) {
        "toString" -> "Proxy@" + System.identityHashCode(delegate)
        "hashCode" -> System.identityHashCode(delegate)
        "equals" -> delegate === args?.getOrNull(0)
        else -> method.invoke(delegate, *(args ?: emptyArray()))
    }
}

/**
 * T Must be interface instance.
 *
 * interface Api {
 *     fun foo()
 * }
 *
 * class ApiImpl : Api {
 *     override fun foo() = println("real foo")
 * }
 *
 *
 * val proxy: Api = real.newDynamicProxy { method, args ->
 *      logD("call ${method.name}")
 *      method.invoke(real, *(args ?: emptyArray()))
 *  }
 *
 */
@Throws(AssertionError::class)
inline fun <reified T : Any> T.newDynamicProxy(
    noinline hook: InvocationHook = defaultHook(this),
): T {
    val iface = T::class.java
    require(iface.isInterface) {
        "Only interface can be used in java dynamic proxy. Actual: ${iface.name}, " +
                "If you are using anonymous class, please specify interface explicitly."
    }

    return Proxy.newProxyInstance(
        iface.classLoader,
        arrayOf(iface)
    ) { proxy, method, args ->
        // 先拦 Object / Any 上的基础方法
        if (method.declaringClass == Any::class.java) {
            when (method.name) {
                "toString" -> "Proxy@${System.identityHashCode(proxy)}(${iface.name})"
                "hashCode" -> System.identityHashCode(proxy)
                "equals" -> proxy === args?.getOrNull(0)
                else -> throw AssertionError("Unexpected Any method: $method")
            }
        } else {
            // 其余方法交给调用方 hook
            hook(method to args)
        }
    } as T
}


/**
 * Retrofit style
 */
inline fun <reified T : Any> newDynamicProxy(
    noinline hook: InvocationHook,
): T = newDynamicProxy(T::class.java, hook)


fun <T : Any> newDynamicProxy(
    iface: Class<T>,
    hook: InvocationHook,
): T {
    require(iface.isInterface) {
        "Only interface can be used in java dynamic proxy. Actual: ${iface.name}"
    }

    @Suppress("UNCHECKED_CAST")
    return Proxy.newProxyInstance(
        iface.classLoader,
        arrayOf(iface)
    ) { proxy, method, args ->
        // 先拦 Object / Any 上的基础方法
        if (method.declaringClass == Any::class.java) {
            when (method.name) {
                "toString" -> "Proxy@${System.identityHashCode(proxy)}(${iface.name})"
                "hashCode" -> System.identityHashCode(proxy)
                "equals" -> proxy === args?.getOrNull(0)
                else -> throw AssertionError("Unexpected Any method: $method")
            }
        } else {
            // 其余方法交给调用方 hook
            hook(method to args)
        }
    } as T
}
