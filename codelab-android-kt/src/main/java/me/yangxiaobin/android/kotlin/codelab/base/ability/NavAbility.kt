package me.yangxiaobin.android.kotlin.codelab.base.ability

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

sealed class FragmentTransition(
    @AnimRes val enter: Int,
    @AnimRes val exit: Int,
    @AnimRes val popEnter: Int,
    @AnimRes val popExit: Int
) {
    // TODO: Replace 0 with actual animation resource IDs
    object SlideRight : FragmentTransition(0, 0, 0, 0)

    // TODO: Replace 0 with actual animation resource IDs
    object SlideUp : FragmentTransition(0, 0, 0, 0)

    // TODO: Replace 0 with actual animation resource IDs
    object Fade : FragmentTransition(0, 0, 0, 0)

    object None : FragmentTransition(0, 0, 0, 0)
}

sealed class Destination {
    data class Activity(
        val clazz: Class<out android.app.Activity>,
        val extras: Bundle? = null,
        val flags: Int? = null,
        val options: Bundle? = null,
        val requestCode: Int? = null,
        val resultLauncher: ActivityResultLauncher<Intent>? = null
    ) : Destination()

    data class Fragment(
        val fragment: androidx.fragment.app.Fragment,
        val addToBackStack: Boolean = true,
        val transition: FragmentTransition = FragmentTransition.SlideRight,
        val tag: String? = null,
        val isReplace: Boolean = false,
    ) : Destination()

    data class Back(
        val tag: String? = null,
        val inclusive: Boolean = false
    ) : Destination()

    object ClearBackStack : Destination()

    data class FinishActivity(
        val resultCode: Int = android.app.Activity.RESULT_CANCELED,
        val data: Intent? = null
    ) : Destination()
}

@DslMarker
annotation class NavigationDsl

@NavigationDsl
class NavigationBuilder {
    val destinations = mutableListOf<Destination>()

    // Activity 导航
    inline fun <reified T : Activity> activity(
        builder: ActivityBuilder.() -> Unit = {}
    ) {
        val activityBuilder = ActivityBuilder(T::class.java).apply(builder)
        destinations.add(activityBuilder.build())
    }

    // Fragment 导航
    fun fragment(
        fragment: Fragment,
        builder: FragmentBuilder.() -> Unit = {}
    ) {
        val fragmentBuilder = FragmentBuilder(fragment).apply(builder)
        destinations.add(fragmentBuilder.build())
    }

    // 返回
    fun back(tag: String? = null, inclusive: Boolean = false) {
        destinations.add(Destination.Back(tag, inclusive))
    }

    // 清空返回栈
    fun clearBackStack() {
        destinations.add(Destination.ClearBackStack)
    }

    // 结束 Activity
    fun finish(resultCode: Int = Activity.RESULT_CANCELED, data: Intent? = null) {
        destinations.add(Destination.FinishActivity(resultCode, data))
    }

    internal fun build() = destinations.toList()
}

@NavigationDsl
class ActivityBuilder(private val clazz: Class<out Activity>) {
    private var extras: Bundle? = null
    private var flags: Int? = null
    private var options: Bundle? = null
    private var requestCode: Int? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    fun extras(builder: Bundle.() -> Unit) {
        extras = Bundle().apply(builder)
    }

    fun extras(bundle: Bundle) {
        extras = bundle
    }

    fun flags(vararg flags: Int) {
        this.flags = flags.reduce { acc, flag -> acc or flag }
    }

    fun options(bundle: Bundle) {
        this.options = bundle
    }

    fun forResult(launcher: ActivityResultLauncher<Intent>) {
        this.resultLauncher = launcher
    }

    @Deprecated("Use forResult with ActivityResultLauncher")
    fun forResult(requestCode: Int) {
        this.requestCode = requestCode
    }

    // 便捷方法
    fun clearTask() {
        flags(Intent.FLAG_ACTIVITY_NEW_TASK, Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }

    fun singleTop() {
        flags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    fun clearTop() {
        flags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }

    fun build() = Destination.Activity(
        clazz, extras, flags, options, requestCode, resultLauncher
    )
}

// ==================== Fragment Builder ====================
@NavigationDsl
class FragmentBuilder(private val fragment: Fragment) {
    private var addToBackStack: Boolean = true
    private var transition: FragmentTransition = FragmentTransition.SlideRight
    private var tag: String? = null
    private var arguments: Bundle? = null

    private var isReplace: Boolean = false

    fun addToBackStack(add: Boolean = true) {
        this.addToBackStack = add
    }

    fun setReplace(replace: Boolean = true) {
        this.isReplace = replace
    }

    // TODO
    fun transition(transition: FragmentTransition) {
        this.transition = transition
    }

    fun tag(tag: String) {
        this.tag = tag
    }

    fun arguments(builder: Bundle.() -> Unit) {
        arguments = Bundle().apply(builder)
    }

    fun arguments(bundle: Bundle) {
        arguments = bundle
    }

    // 便捷方法
    fun noBackStack() {
        addToBackStack = false
    }

    fun slideUp() {
        transition = FragmentTransition.SlideUp
    }

    fun fade() {
        transition = FragmentTransition.Fade
    }

    fun noAnimation() {
        transition = FragmentTransition.None
    }

    internal fun build(): Destination.Fragment {
        // 设置 arguments
        arguments?.let { fragment.arguments = it }

        return Destination.Fragment(
            fragment,
            addToBackStack,
            transition,
            tag ?: fragment::class.java.simpleName,
            isReplace,
        )
    }
}

// ==================== 导航执行器 ====================
class AppNavigator(
    private val activity: FragmentActivity,
    @IdRes private val containerId: Int
) : LogAbility {

    private val fragmentManager get() = activity.supportFragmentManager

    // DSL 入口
    fun navigate(block: NavigationBuilder.() -> Unit) {
        val destinations = NavigationBuilder().apply(block).build()

        destinations.forEach { destination ->
            executeNavigation(destination)
        }
    }

    private fun executeNavigation(destination: Destination) {
        when (destination) {
            is Destination.Activity -> navigateToActivity(destination)
            is Destination.Fragment -> navigateToFragment(destination)
            is Destination.Back -> navigateBack(destination)
            is Destination.ClearBackStack -> clearBackStack()
            is Destination.FinishActivity -> finishActivity(destination)
        }
    }

    // ==================== Activity 导航实现 ====================
    private fun navigateToActivity(dest: Destination.Activity) {
        Intent(activity, dest.clazz).apply {
            dest.extras?.let { putExtras(it) }
            dest.flags?.let { flags = it }

            when {
                dest.resultLauncher != null -> {
                    dest.resultLauncher.launch(this)
                }

                dest.requestCode != null -> {
                    @Suppress("DEPRECATION")
                    activity.startActivityForResult(this, dest.requestCode)
                }

                else -> {
                    activity.startActivity(this, dest.options)
                }
            }
        }

        logD("Navigated to Activity: ${dest.clazz.simpleName}")
    }

    // ==================== Fragment 导航实现 ====================
    private fun navigateToFragment(dest: Destination.Fragment) {
        fragmentManager.commit {
            if (dest.transition != FragmentTransition.None) {
                setCustomAnimations(
                    dest.transition.enter,
                    dest.transition.exit,
                    dest.transition.popEnter,
                    dest.transition.popExit
                )
            }

            if (dest.isReplace) {
                replace(containerId, dest.fragment, dest.tag)
            } else {
                add(containerId, dest.fragment, dest.tag)
            }

            if (dest.addToBackStack) {
                addToBackStack(dest.tag)
            }

            setReorderingAllowed(true)
        }

        logD("Navigated to Fragment: ${dest.fragment::class.java.simpleName}")
    }

    // ==================== 返回导航实现 ====================
    private fun navigateBack(dest: Destination.Back) {
        when {
            dest.tag != null -> {
                val flags = if (dest.inclusive) {
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                } else {
                    0
                }
                fragmentManager.popBackStack(dest.tag, flags)
                logD("Navigated back to tag: ${dest.tag}")
            }

            fragmentManager.backStackEntryCount > 0 -> {
                fragmentManager.popBackStack()
                logD("Navigated back")
            }

            else -> {
                activity.finish()
                logD("No back stack, finishing activity")
            }
        }
    }

    // ==================== 清空返回栈 ====================
    private fun clearBackStack() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        logD("Cleared back stack")
    }

    // ==================== 结束 Activity ====================
    private fun finishActivity(dest: Destination.FinishActivity) {
        activity.setResult(dest.resultCode, dest.data)
        activity.finish()
        logD("Finished activity with result code: ${dest.resultCode}")
    }

    // ==================== 便捷方法 ====================
    fun getCurrentFragment(): Fragment? {
        return fragmentManager.findFragmentById(containerId)
    }

    fun getBackStackCount(): Int {
        return fragmentManager.backStackEntryCount
    }
}

// Api usage below.
// 如果需要更简洁的调用方式
inline fun <reified T : Activity> NavigationBuilder.activitySimple(
    extras: Bundle? = null
) {
    activity<T> {
        extras?.let { extras(it) }
    }
}

fun NavigationBuilder.fragmentSimple(
    fragment: Fragment,
    args: Bundle? = null
) {
    fragment(fragment) {
        args?.let { arguments(it) }
    }
}


interface NavAbility {
    val navHostActivity: FragmentActivity
    val navFragmentHostContainerId: Int

    companion object NavigatorHolder {
        private val cache = mutableMapOf<Int, AppNavigator>()

        fun getOrCreate(ability: NavAbility): AppNavigator {
            val key = System.identityHashCode(ability.navHostActivity)
            return cache.getOrPut(key) {
                AppNavigator(ability.navHostActivity, ability.navFragmentHostContainerId)
            }
        }
    }
}

// 扩展属性，使用 lazy
val NavAbility.navigator: AppNavigator get() = NavAbility.NavigatorHolder.getOrCreate(this)

// 扩展方法
fun NavAbility.navigate(block: NavigationBuilder.() -> Unit) {
    navigator.navigate(block)
}

