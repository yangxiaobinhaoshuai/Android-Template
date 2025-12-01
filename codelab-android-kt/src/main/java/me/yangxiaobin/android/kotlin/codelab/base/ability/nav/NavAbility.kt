package me.yangxiaobin.android.kotlin.codelab.base.ability.nav

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.util.WeakHashMap


class Navigator(
    private val engine: NavEngine
) {

    suspend fun <A : NavArgs, R> startActivity(
        route: ActivityRoute<A, R>,
        args: A
    ): R = engine.execute(
        NavCommand.StartActivity(route, args)
    )

    suspend fun <A : NavArgs, R> showFragment(
        route: FragmentRoute<A, R>,
        args: A,
        containerId: Int,
        addToBackStack: Boolean = true,
        replace: Boolean = false,
    ): R = engine.execute(
        NavCommand.ShowFragment(route, args, containerId, addToBackStack, replace)
    )

    suspend fun <A : NavArgs, R> showDialog(
        route: DialogRoute<A, R>,
        args: A,
        tag: String? = null,
    ): R = engine.execute(
        NavCommand.ShowDialog(route, args, tag)
    )

    suspend fun popBackStack(): Unit = engine.execute(NavCommand.PopBackStack)

    // 非 suspend 版，只给 Unit 返回值的 route 用
    fun <A : NavArgs> startActivityNow(
        route: ActivityRoute<A, Unit>,
        args: A
    ) {
        engine.executeNow(NavCommand.StartActivity(route, args))
    }

    fun <A : NavArgs> showFragmentNow(
        route: FragmentRoute<A, Unit>,
        args: A,
        containerId: Int,
        addToBackStack: Boolean = true,
        replace: Boolean = false,
    ) {
        engine.executeNow(
            NavCommand.ShowFragment(route, args, containerId, addToBackStack, replace)
        )
    }

    fun <A : NavArgs> showDialogNow(
        route: DialogRoute<A, Unit>,
        args: A,
        tag: String? = null,
    ) {
        engine.executeNow(
            NavCommand.ShowDialog(route, args, tag)
        )
    }

    fun popBackStackNow() {
        engine.executeNow(NavCommand.PopBackStack)
    }

}

private val engineCache = WeakHashMap<FragmentActivity, NavEngine>()
private val navigatorCache = WeakHashMap<FragmentActivity, Navigator>()

fun createDefaultNavEngine(activity: FragmentActivity): NavEngine {
    val activityDriver = DefaultActivityNavDriver(activity)
    val fragmentDriver = DefaultFragmentNavDriver(
        fragmentManager = activity.supportFragmentManager,
        resultLifecycleOwner = activity,
    )
    val dialogDriver = DefaultDialogNavDriver(
        fragmentManager = activity.supportFragmentManager,
        resultLifecycleOwner = activity,
    )
    // 如果你还有 DialogDriver，也在这里一起 new
    return DefaultNavEngine(
        activityDriver = activityDriver,
        fragmentDriver = fragmentDriver,
        dialogDriver = dialogDriver,
    )
}

interface NavAbility {

    val FragmentActivity.navEngine: NavEngine
        get() {
            return engineCache.getOrPut(this) {
                createDefaultNavEngine(this)
            }
        }

    val FragmentActivity.navigator: Navigator
        get() {
            return navigatorCache.getOrPut(this) {
                Navigator(navEngine)
            }
        }

    val Fragment.navigator: Navigator
        get() {
            val activity = requireActivity()
            return activity.navigator
        }

    val Fragment.engine: NavEngine
        get() {
            val activity = requireActivity()
            return activity.navEngine
        }

}

