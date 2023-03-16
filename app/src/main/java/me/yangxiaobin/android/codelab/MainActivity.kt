package me.yangxiaobin.android.codelab

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import me.yangxiaobin.android.codelab.common.ComposeVerticalListFragment
import me.yangxiaobin.android.kotlin.codelab.base.AbsActivity
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.getActionString
import me.yangxiaobin.android.kotlin.codelab.ext.mainHandler
import me.yangxiaobin.android.kotlin.codelab.ext.uicontroller.showActivityToast
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.setSimpleDivider
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.kotlin.codelab.recyclerview.AbsVH
import me.yangxiaobin.android.kotlin.codelab.recyclerview.SimpleRvAdapter
import me.yangxiaobin.logger.core.LogFacade


class MainActivity : AbsActivity() {

    private val catalog: Map<String, Array<String>> = mapOf(

        // 1. RecyclerView
        "RecyclerView" to arrayOf(
            "LinearRv",
            "LinearRvStickyTailFragment",
            "DraggableGridRV",
            "NormalGridRv",
        ),

        // 2. Multi process
        "Multi Process" to arrayOf(
            "Remote Activity",
            "Local Service",
            "Remote Service",
            "Local ContentProvider",
            "Remote ContentProvider",
        ),

        // 3. DI
        "DI" to arrayOf(
            "Dagger2",
            "Dagger2.android",
            "Hilt",
        ),

        // 4. Kt Jetpack Components
        "Kotlin Jetpack Components" to arrayOf(
            "LifeCycle",
            "LiveData",
            "ViewModel",
            "Paging3",
            "Room",
            "Navigation",
            "MutableSharedFlow",
            "Flow",
        ),

        // 5. Kt Jetpack Compose
        "Kotlin Jetpack Compose" to arrayOf(
            "MyBottomSheetDialogFragment",
            "RoundCornerBSDF",
            "Compose CheckBox",
        ),

        // 6. Retrofit
        "Retrofit" to arrayOf(
            "CustomConverter",
        ),

        // 7. Multi Threads
        "Multi Threads" to arrayOf(
            "Thread",
            "ReentrantLock",
            "Future",
        ),

        // 8. Android alerts
        "Alerts" to arrayOf(
            "PopupWindow",
        ),

        // 9. Android Touch Events
        "Touch Events" to arrayOf(
            "ACTION_CANCEL",
            "ContinuousClick",
            "ScaleGesture",
            "ViewDragHelperFragment",
            "ViewDragFragment",
        ),

        // 10. qrcode scan
        "QRCode" to arrayOf(
            "PermissionRequest",
            "QRCodeScanActivity",
        ),

        // 11. Proguard
        "Proguard" to arrayOf(
            "Reflection",
        ),

        // 12. Permission example
        "Permission examples" to arrayOf(
            "MIUI Privacy Protection"
        ),

        // 13. Jank sample
        "Jank Samples" to arrayOf(
            "Perfetto Sample",
            "NPE",
        ),

        // 14. webView
        "WebView" to arrayOf(
            "System WebView",
            "Js Function",
        ),

        // 15. LogTest
        "Log" to arrayOf(
            "LogTest",
        ),

        // 16. Widgets
        "Widgets" to arrayOf(
            "TabLayout_OppoEmbed",
            "SelectorsFragment",
            "EditTextFragment",
        ),

        // 17. Keyboard
        "Keyboard" to arrayOf(
          "KeyboardHeight",
          "KeyboardActivity",
          "BottomSheetDialogFragment_Keyboard",
        ),

        // 18. Proxy
        "Proxy" to arrayOf(
            "DynamicProxy",
        ),

        // 19. Image
        "Image" to arrayOf(
            "ImageEdit",
            "Matrix",
            "DrawableFragment",
        ),

        // 20.Animator
        "Animator" to arrayOf(
            "AnimatorExampleFragment",
            "AnimatorExampleActivity",
        ),

        // 21. Reflection
        "Reflection" to arrayOf(
            "ReflectionFragment",
        ),

        // 22. Router
        "Router" to arrayOf(
            "startActivityForResult",
        ),

        // 23. Canvas
        "Canvas" to arrayOf(
            "RectFragment",
        ),

        // 24. Flutter
       "Flutter" to arrayOf(
           "FlutterMain",
           "FlutterList",
           "FlutterButton",
       ),

    )


    override val LogAbility.TAG: String get() = "Sample-app"

    override val logger: LogFacade get() = AndroidLogger

    override val contentResId: Int get() = R.layout.activity_main

    override val handleBackPress: Boolean get() = this.isTaskRoot

    override fun onHandleBackPress() {
        super.onHandleBackPress()
        this.moveTaskToBack(true)
    }

    override fun afterOnCreate() {
        super.afterOnCreate()
        init()
    }


    private fun init() {
        val rv = rv_main_activity

        rv.setSimpleDivider()
        rv.layoutManager = LinearLayoutManager(this)

        rv.adapter = SimpleRvAdapter(
            catalog.keys.toList(),
            android.R.layout.simple_list_item_1
        ) { (vh: AbsVH, entity: String?, pos: Int, _: MutableList<Any>) ->

            vh.requireView<TextView>(android.R.id.text1).run {

                @SuppressLint("SetTextI18n")
                text = "${pos + 1}. $entity"

                setOnClickListener {
                    val subMenus: Array<String> = catalog.values.toList()[pos]
                    this@MainActivity.navigateToFragment(ComposeVerticalListFragment(), "subMenus" to subMenus)
                }
            }

        }


        // Scroll to last one.
        rv.scrollToPosition(rv.adapter?.itemCount?.minus(1) ?: 0)


        // TODO config this pls.
        navigateToSubFragment("FlutterButton")
    }

    @Suppress("SameParameterValue")
    private fun navigateToSubFragment(identify: String) {
        catalog.toList()
            .find { (_,subMenus): Pair<String, Array<String>> -> subMenus.any { it == identify } }
            ?.let { (_, subMenus) ->
                val target = ComposeVerticalListFragment()
                this@MainActivity.navigateToFragment(target, "subMenus" to subMenus)
                mainHandler.post { showActivityToast("Quick navi to $identify.");target.naviToDestFragment(identify)
                }
            } ?: showActivityToast("Can't match submenu: $identify")
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev).also { logI("ActionCancelFragment, MainActivity, dispatchTouchEvent:$it, ${ev.getActionString}.") }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event).also { logI("ActionCancelFragment, MainActivity, onTouchEvent:$it, ${event.getActionString}.") }
    }

    override fun checkPermission(permission: String, pid: Int, uid: Int): Int {
        //logD("CheckPermission, permission :$permission, stacktrace :${getLimitStacktrace(20)}.")
        return super.checkPermission(permission, pid, uid)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) logD("onKeyDown, keyCode :$keyCode.")
        return super.onKeyDown(keyCode, event)
    }

}


fun FragmentActivity.navigateToFragment(
    target: Fragment,
    vararg pairs: Pair<String, Any?>
) = supportFragmentManager.commit {
    target.arguments = bundleOf(*pairs)
    addToBackStack(null)
    add(R.id.content_main_activity, target)
}
