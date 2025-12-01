package me.yangxiaobin.android.kotlin.codelab.base.ability.nav

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * 导航引擎：统一执行 NavCommand。
 */
interface NavEngine {
    suspend fun <R> execute(command: NavCommand<R>): R

    fun <R> executeNow(command: NavCommand<R>): R
}

/**
 * 一个导航命令：要做什么事，以及期望返回什么类型 R。
 */
sealed class NavCommand<R> {

    data class StartActivity<A : NavArgs, R>(
        val route: ActivityRoute<A, R>,
        val args: A,
    ) : NavCommand<R>()

    data class ShowFragment<A : NavArgs, R>(
        val route: FragmentRoute<A, R>,
        val args: A,
        val containerId: Int,
        val addToBackStack: Boolean = true,
        val replace: Boolean = true,
    ) : NavCommand<R>()

    object PopBackStack : NavCommand<Unit>()
}


interface NavArgs {
    fun toBundle(): Bundle
    interface Factory<A : NavArgs> {
        fun fromBundle(bundle: Bundle): A
    }

    companion object NoArgs : NavArgs {
        override fun toBundle(): Bundle = Bundle.EMPTY

        object Factory : NavArgs.Factory<NoArgs> {
            override fun fromBundle(bundle: Bundle): NoArgs = NoArgs
        }
    }
}

data class SimpleStringNavParam(val value: String) : NavArgs {
    override fun toBundle(): Bundle {
        return Bundle().apply {
            putString("value", value)
        }
    }

    companion object Factory : NavArgs.Factory<SimpleStringNavParam> {
        override fun fromBundle(bundle: Bundle): SimpleStringNavParam {
            val value = bundle.getString("value") ?: ""
            return SimpleStringNavParam(value)
        }
    }
}

object NoArgs : NavArgs {
    override fun toBundle(): Bundle = Bundle.EMPTY

    object Factory : NavArgs.Factory<NoArgs> {
        override fun fromBundle(bundle: Bundle): NoArgs = NoArgs
    }
}


typealias IntentBuilder = (Context) -> Intent
typealias ActivityResultDecoder<R> = (resultCode: Int, data: Intent?) -> R

data class ActivityRoute<A : NavArgs, R>(
    val buildIntent: IntentBuilder,
    val decodeResult: ActivityResultDecoder<R>,
)

fun activityRoute(build: IntentBuilder) =
    activityRoute<NavArgs.NoArgs, Unit>(
        build = build,
        resultDecoder = { _: Int, _: Intent? -> Unit }, // 返回 Unit
    )

inline fun <reified A : NavArgs, reified R> activityRoute(
    noinline build: IntentBuilder,
    noinline resultDecoder: ActivityResultDecoder<R>,
): ActivityRoute<A, R> = ActivityRoute(build, resultDecoder)


typealias FragmentFactory = () -> Fragment
typealias FragmentResultDecoder<R> = (Bundle) -> R

data class FragmentRoute<A : NavArgs, R>(
    val createFragment: FragmentFactory,
    val decodeResult: FragmentResultDecoder<R>,
)


inline fun <reified A : NavArgs> fragmentRoute(noinline create: FragmentFactory) =
    fragmentRoute<A, Unit>(
        create = create,
        decodeResult = { _: Bundle -> Unit },
    )

inline fun <reified A : NavArgs, reified R> fragmentRoute(
    noinline create: FragmentFactory,
    noinline decodeResult: FragmentResultDecoder<R>,
): FragmentRoute<A, R> = FragmentRoute(create, decodeResult)
