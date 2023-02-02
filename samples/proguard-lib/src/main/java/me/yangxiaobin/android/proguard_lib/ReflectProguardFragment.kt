package me.yangxiaobin.android.proguard_lib

import android.view.View
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.uicontroller.showFragmentToast
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyFragment
import me.yangxiaobin.logger.core.LogFacade
import java.lang.reflect.Method

class ReflectProguardFragment : EmptyFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "ReflectFragment"


    override fun onButtonClick(index: Int, v: View) {
        super.onButtonClick(index,v)

        when (index) {
            0 -> testForReflection()
            1 -> testForReflection2()
            else -> {}
        }
    }

    private fun testForReflection() {

        val res = try {
            val forTestClazz: Class<*> =
                Class.forName("ForTest")

            val forTestInstance = forTestClazz.newInstance()
            val testMethod: Method = forTestClazz.getMethod("test")

            testMethod.invoke(forTestInstance)
        } catch (e: Exception) {
            logD("testForReflection, error: ${e.stackTraceToString()}.")
            e.toString()
        }

        logD("testForReflection, res: $res.")

        showFragmentToast("res :$res")
    }

    private fun testForReflection2() {

        val res = try {
            val testMethod: Method = ForTest::class.java.getDeclaredMethod("test")

            testMethod.invoke(ForTest())
        } catch (e: Exception) {
            logD("testForReflection2, error: ${e.stackTraceToString()}.")
            e.toString()
        }

        showFragmentToast("res :$res")
    }
}


//@Keep
class ForTest {

    fun test(): String = "I'm Fortest.test().."
}
