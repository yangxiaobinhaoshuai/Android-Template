package me.yangxiaobin.android.proxylib

import android.view.View
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyFragment
import me.yangxiaobin.logger.core.LogFacade
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * @see java.lang.reflect.Proxy
 * @see java.lang.reflect.InvocationHandler
 * @see retrofit2.Retrofit
 *
 * blog ：https://www.jianshu.com/p/9bcac608c714
 */
class DynamicProxyFragment : EmptyFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "DynamicProxy"


    override fun onButtonClick(index: Int, v: View) {
        super.onButtonClick(index, v)
        when (index) {
            0 -> {

                logD("Click 0 pos.")

                val handler =
                    InvocationHandler { proxy, method, args -> TODO("Not yet implemented") }

                val anonymousTest = object : ITest {
                    override fun test(param: Int): Int {
                        logD("I'm anonymousTest...")
                        return param * param
                    }
                }

                val a = Any()

                val t: Any = Proxy.newProxyInstance(
                    ITest::class.java.classLoader, arrayOf(ITest::class.java)
                ) { proxy: Any?, method: Method, args: Array<Any> ->
                    logD("====>  proxy:  method: $method, args ")
//                    val res = method.invoke(anonymousTest, *args)
//                    logD("method invocation res: $res.")
//                    res
                    1
                }

                t as ITest
                logD("t's type : ${t.javaClass}")
                t.test(123)

//                val t1 = newProxy<ITest>()
//                val r1 = t1.test(1)
//                logD("r1 : $r1")

            }


            1 -> {}
            else -> Unit
        }
    }

}


interface ITest {
    fun test(param: Int): Int
}
