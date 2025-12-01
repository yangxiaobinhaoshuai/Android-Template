package me.yangxiaobin.android.kotlin.codelab.base.ability.nav

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

interface DialogNavDriver {
    suspend fun <A : NavArgs, R> showDialog(
        route: DialogRoute<A, R>,
        args: A,
        tag: String? = null,
    ): R

    fun <A : NavArgs> showDialogNow(
        route: DialogRoute<A, Unit>,
        args: A,
        tag: String? = null,
    )
}


class DefaultDialogNavDriver(
    private val fragmentManager: FragmentManager,
    private val resultLifecycleOwner: LifecycleOwner,
) : DialogNavDriver {

    override suspend fun <A : NavArgs, R> showDialog(
        route: DialogRoute<A, R>,
        args: A,
        tag: String?,
    ): R = suspendCancellableCoroutine { cont ->

        // 1. 为本次导航生成唯一 requestKey
        val requestKey =
            "nav-dialog-${System.identityHashCode(cont)}-${System.currentTimeMillis()}"

        // 2. 注册 result listener
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

        // 3. 创建 DialogFragment，并把 args + requestKey 塞进去
        val dialog: DialogFragment = route.createDialog().apply {
            arguments = (arguments ?: Bundle()).apply {
                putAll(args.toBundle())
                // 这里复用你 Fragment 那套的 requestKey key
                // 如果你有 NavConstants.ARG_REQUEST_KEY，就用那个
                putString(NavConstants.ARG_REQUEST_KEY, requestKey)
            }
        }.also {

            // 4. 展示 Dialog
            it.show(fragmentManager, tag ?: it.javaClass.name)
        }

        // 5. 协程取消时的清理逻辑
        cont.invokeOnCancellation {
            runCatching {
                fragmentManager.clearFragmentResultListener(requestKey)
                // 如果此时 dialog 仍在展示，可以选择关闭掉（防止泄漏）
                dialog.dismissAllowingStateLoss()
            }
        }
    }

    override fun <A : NavArgs> showDialogNow(
        route: DialogRoute<A, Unit>,
        args: A,
        tag: String?,
    ) {
        val dialog: DialogFragment = route.createDialog().apply {
            arguments = (arguments ?: Bundle()).apply {
                putAll(args.toBundle())
            }
        }

        dialog.show(fragmentManager, tag ?: dialog.javaClass.name)
    }
}