package me.yangxiaobin.android.kotlin.codelab.base.ability.nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


interface FragmentNavDriver {
    suspend fun <A : NavArgs, R> showFragment(
        route: FragmentRoute<A, R>,
        args: A,
        containerId: Int,
        addToBackStack: Boolean = true,
        replace: Boolean = true,
    ): R

    fun <A : NavArgs> showFragmentNow(
        route: FragmentRoute<A, Unit>,
        args: A,
        containerId: Int,
        addToBackStack: Boolean = true,
        replace: Boolean = true,
    )

    fun popBackStack(): Boolean
}

class DefaultFragmentNavDriver(
    private val fragmentManager: FragmentManager,
    private val resultLifecycleOwner: LifecycleOwner,
) : FragmentNavDriver {

    override suspend fun <A : NavArgs, R> showFragment(
        route: FragmentRoute<A, R>,
        args: A,
        containerId: Int,
        addToBackStack: Boolean,
        replace: Boolean,
    ): R = suspendCancellableCoroutine { cont ->

        val requestKey: String = "nav-fragment-${UUID.randomUUID()}"

        // 1. 注册 FragmentResultListener，等待结果
        fragmentManager.setFragmentResultListener(
            requestKey,
            resultLifecycleOwner
        ) { key, bundle ->
            if (key != requestKey) return@setFragmentResultListener

            fragmentManager.clearFragmentResultListener(requestKey)

            try {
                val value: R = route.decodeResult(bundle)
                cont.resume(value)
            } catch (t: Throwable) {
                cont.resumeWithException(t)
            }
        }

        // 2. 创建 Fragment + 写入 args 和 requestKey
        val fragment = route.createFragment().apply {
            arguments = (arguments ?: Bundle()).apply {
                putAll(args.toBundle())
                putString(NavConstants.ARG_REQUEST_KEY, requestKey)
            }
        }

        // 3. 入栈
        fragmentManager.commit {
            if (replace) {
                replace(containerId, fragment, fragment.javaClass.name)
            } else {
                add(containerId, fragment, fragment.javaClass.name)
            }
            if (addToBackStack) {
                addToBackStack(fragment.javaClass.name)
            }
        }

        // 4. 取消时清理 listener
        cont.invokeOnCancellation {
            fragmentManager.clearFragmentResultListener(requestKey)
        }
    }

    override fun <A : NavArgs> showFragmentNow(
        route: FragmentRoute<A, Unit>,
        args: A,
        containerId: Int,
        addToBackStack: Boolean,
        replace: Boolean,
    ) {
        val fragment = route.createFragment().apply {
            arguments = (arguments ?: Bundle()).apply {
                putAll(args.toBundle())
                // 无返回值，不需要 requestKey
            }
        }

        fragmentManager.commit {
            if (replace) {
                replace(containerId, fragment, fragment.javaClass.name)
            } else {
                add(containerId, fragment, fragment.javaClass.name)
            }
            if (addToBackStack) {
                addToBackStack(fragment.javaClass.name)
            }
        }
    }

    override fun popBackStack(): Boolean =
        fragmentManager.popBackStackImmediate()
}


object NavConstants {
    const val ARG_REQUEST_KEY = "_nav_arg_request_key_"
}

/**
 * 在目标 Fragment 中调用，用来把结果发回去。
 *
 * R 的序列化规则交给 FragmentRoute.packResult。
 */
fun Fragment.sendNavResult(bundle: Bundle) {
    val key = requireArguments().getString(NavConstants.ARG_REQUEST_KEY)
        ?: error("No requestKey found in arguments, nav not started by suspend nav method in NavDriver?")
    setFragmentResult(key, bundle)
}