package me.yangxiaobin.android.codelab

import android.annotation.SuppressLint
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
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.getActionString
import me.yangxiaobin.android.kotlin.codelab.ext.setSimpleDivider
import me.yangxiaobin.android.kotlin.codelab.recyclerview.AbsVH
import me.yangxiaobin.android.kotlin.codelab.recyclerview.SimpleRvAdapter


class MainActivity : AbsActivity() {


    private val catalog: Map<String, Array<String>> = mapOf(

        // 1.
        "RecyclerView" to arrayOf(
            "LinearRv",
            "GridRv",
        ),

        // 2.
        "Multi Process" to arrayOf(
            "Remote Activity",
            "Local Service",
            "Remote Service",
            "Local ContentProvider",
            "Remote ContentProvider",
        ),

        // 3.
        "DI" to arrayOf(
            "Dagger2",
            "Dagger2.android",
            "Hilt",
        ),

        // 4.
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

        // 5.
        "Kotlin Jetpack Compose" to arrayOf(
            "MyBottomSheetDialogFragment",
            "Compose CheckBox",
        ),

        // 6.
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
           "ACTION_CANCEL"
       )
    )


    override val LogAbility.TAG: String get() = "Sample-app"

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
                    this@MainActivity.navigateToFragment(
                        ComposeVerticalListFragment(),
                        "subMenus" to subMenus
                    )
                }
            }


        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev).also { logI("ActionCancelFragment, MainActivity, dispatchTouchEvent:$it, ${ev.getActionString}.") }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event).also { logI("ActionCancelFragment, MainActivity, onTouchEvent:$it, ${event.getActionString}.") }
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
