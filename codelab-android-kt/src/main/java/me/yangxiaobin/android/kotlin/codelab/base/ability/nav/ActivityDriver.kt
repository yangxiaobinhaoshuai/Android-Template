package me.yangxiaobin.android.kotlin.codelab.base.ability.nav

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * 专门处理 Activity 导航的 Driver。
 *
 * 注意：不暴露任何 ActivityResult API 给外部，调用者只用 suspend。
 */
interface ActivityNavDriver {
    // 有返回值（或你想挂起等结果）的，用这个
    suspend fun <A : NavArgs, R> startActivity(
        route: ActivityRoute<A, R>,
        args: A,
    ): R

    // 单纯 fire-and-forget（R = Unit）的，用这个
    fun <A : NavArgs> startActivityNow(
        route: ActivityRoute<A, Unit>,
        args: A,
    )
}


class DefaultActivityNavDriver(
    private val activity: FragmentActivity, // 或 AppCompatActivity
) : ActivityNavDriver {

    override suspend fun <A : NavArgs, R> startActivity(
        route: ActivityRoute<A, R>,
        args: A,
    ): R = suspendCancellableCoroutine { cont ->

        // 1. 构建 Intent + 写入 args
        val intent = route.buildIntent(activity).apply {
            putExtras(args.toBundle())
        }

        // 2. 注册 Activity Result 回调
        val key = "nav-activity-${hashCode()}-${System.identityHashCode(cont)}"
        val registry = activity.activityResultRegistry

        lateinit var launcher: ActivityResultLauncher<Intent>

        launcher = registry.register(
            key,
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            try {
                val value: R = route.decodeResult(
                    result.resultCode,
                    result.data
                )
                cont.resume(value)
            } catch (t: Throwable) {
                cont.resumeWithException(t)
            } finally {
                launcher.unregister()
            }
        }

        // 3. 发起跳转
        launcher.launch(intent)

        // 4. 取消时清理
        cont.invokeOnCancellation {
            runCatching { launcher.unregister() }
        }
    }

    override fun <A : NavArgs> startActivityNow(
        route: ActivityRoute<A, Unit>,
        args: A,
    ) {
        val intent = route.buildIntent(activity).apply {
            putExtras(args.toBundle())
        }
        activity.startActivity(intent)
    }
}