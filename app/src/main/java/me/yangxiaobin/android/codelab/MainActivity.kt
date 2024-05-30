package me.yangxiaobin.android.codelab

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Build
import android.view.DisplayCutout
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.WindowDecorActionBar
import androidx.core.os.bundleOf
import androidx.core.os.postDelayed
import androidx.core.view.DisplayCutoutCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import me.yangxiaobin.android.codelab.common.ComposeVerticalListFragment
import me.yangxiaobin.android.kotlin.codelab.base.AbsActivity
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.context.statusBarSize
import me.yangxiaobin.android.kotlin.codelab.ext.getActionString
import me.yangxiaobin.android.kotlin.codelab.ext.mainHandler
import me.yangxiaobin.android.kotlin.codelab.ext.uicontroller.showActivityToast
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.setSimpleDivider
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.kotlin.codelab.recyclerview.SimpleRvAdapter
import me.yangxiaobin.android.kotlin.codelab.recyclerview.SimpleVH
import me.yangxiaobin.logger.core.LogFacade

/**
 *  java.lang.IllegalStateException: Hilt Fragments must be attached to an @AndroidEntryPoint Activity.
 *  Found:class me.yangxiaobin.android.codelab.MainActivity
 */
@AndroidEntryPoint
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
            "HiltFragment",
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
        ),

        // 11. Proguard
        "Proguard" to arrayOf(
            "Reflection",
        ),

        // 12. Permission example
        "System relevant" to arrayOf(
            "MIUI Privacy Protection",
            "ClipBoard Copy",
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
            "Matrix",
        ),

        // 20.Animator
        "Animator" to arrayOf(
            "AnimatorExampleFragment",
            "AnimatorExampleActivity",
            "TranslateWaysFragment",
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
            "CharSequence",
            "CanvasFragment",
        ),

        // 24.drawable
        "Drawable" to arrayOf(
            "DrawableOpacity",
        ),

        // 25. MotionEvent
        "MotionEvent" to arrayOf(
            "GestureDetector.onSingleTap",
        ),

        // 26. StackTrace
        "StackTrace" to arrayOf(
            "LastMethodFrame",
            "CharacterSelection"
        ),
        // 27. Sys function
        "SysFunction" to arrayOf(
            "KtLoop",
        ),
        // 28. Device Info
        "DeviceInfo" to arrayOf(
            "AppSignature",
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
        notchDetect()
        init()
    }

    private fun hideSystemBars() {
        try {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        } catch (e: Exception) {
            logE("Can not hide system bars. ${e.stackTraceToString()}")
        }
    }

    /**
     * 适配刘海屏
     */
    private fun notchDetect() {
        hideSystemBars()
        window.decorView.setOnApplyWindowInsetsListener { v, insets: WindowInsets ->
            logD("setOnApplyWindowInsetsListener, insets: $insets.")
            insets
        }
        window.decorView.doOnAttach {

        }

        window.decorView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                logD("decorView onViewAttachedToWindow.")
            }

            override fun onViewDetachedFromWindow(v: View) {
                logD("decorView onViewDetachedFromWindow.")
            }

        })

        logD("notchDetect, sdk int: ${Build.VERSION.SDK_INT}.")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val wd: Window = window
            logD("MainActivity window: $wd.")
            val attr: WindowManager.LayoutParams = wd.attributes
            attr.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            //attr.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
            //attr.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
            //attr.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT

            val windowInsetsCompat: WindowInsetsCompat = WindowInsetsCompat.CONSUMED
            logD("MainActivity windowInsetsCompat: $windowInsetsCompat.")
            val displayCutout: DisplayCutoutCompat? = windowInsetsCompat.displayCutout
            logD("MainActivity displayCutout: $displayCutout.")

            val cutout: DisplayCutout? = window.decorView.rootWindowInsets?.displayCutout
            logD("MainActivity cutout: $cutout.")

            mainHandler.postDelayed(0) {
                val c: DisplayCutout? = window.decorView.rootWindowInsets?.displayCutout
                val d: DisplayCutoutCompat? = windowInsetsCompat.displayCutout
                logD("MainActivity postDelay displayCutout: $d, $c")
                //attr.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }


        } else {
            logE("Api under 28, can NOT support notch detection.")
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val cutout: DisplayCutout? = window.decorView.rootWindowInsets.displayCutout
            logD("MainActivity onAttachedToWindow, cutout: $cutout.")
            if (cutout != null){
                val boundingRectTop: Rect = cutout.boundingRectTop
                logD("MainActivity onAttachedToWindow, cutout.boundingRectTop: $boundingRectTop.")
                logD("MainActivity onAttachedToWindow, statusBarHeight: ${this.statusBarSize}.")
            }

            val wd: Window = window
            val attr: WindowManager.LayoutParams = wd.attributes
            //attr.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
    }

    private fun init() {
        val rv = findViewById<RecyclerView>(R.id.rv_main_activity)

        rv.setSimpleDivider()
        rv.layoutManager = LinearLayoutManager(this)

        rv.adapter = SimpleRvAdapter(
            catalog.keys.toList(),
            android.R.layout.simple_list_item_1
        ) { (vh: SimpleVH, entity: String?, pos: Int, _: MutableList<Any>) ->

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
        navigateToSubFragment("AppSignature")
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

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev).also { logI("ActionCancelFragment, MainActivity, dispatchTouchEvent:$it, ${ev.getActionString}.") }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
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


/**
 * allowStateLoss = true  for java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
 */
fun FragmentActivity.navigateToFragment(
    target: Fragment,
    vararg pairs: Pair<String, Any?>
) = supportFragmentManager.commit(allowStateLoss = true) {
    target.arguments = bundleOf(*pairs)
    addToBackStack(null)
    add(R.id.content_main_activity, target)
}
