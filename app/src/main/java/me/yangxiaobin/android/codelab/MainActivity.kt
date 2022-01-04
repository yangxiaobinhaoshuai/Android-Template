package me.yangxiaobin.android.codelab

import android.annotation.SuppressLint
import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import me.yangxiaobin.android.codelab.common.BaseClick
import me.yangxiaobin.android.codelab.common.ComposeVerticalListFragment
import me.yangxiaobin.android.codelab.compose.MyBottomSheetDialogFragment
import me.yangxiaobin.android.codelab.multi_process.LocalService
import me.yangxiaobin.android.codelab.multi_process.RemoteActivity
import me.yangxiaobin.android.codelab.multi_process.RemoteService
import me.yangxiaobin.android.codelab.recyclerview.GridRvFragment
import me.yangxiaobin.android.codelab.recyclerview.LinearRvFragment
import me.yangxiaobin.android.codelab.recyclerview.PagingRvFragment
import me.yangxiaobin.android.kotlin.codelab.base.AbsActivity
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.setSimpleDivider
import me.yangxiaobin.android.kotlin.codelab.ext.showToast
import me.yangxiaobin.android.kotlin.codelab.recyclerview.AbsVH
import me.yangxiaobin.android.kotlin.codelab.recyclerview.SimpleRvAdapter
import org.jetbrains.anko.intentFor

class MainActivity : AbsActivity() {

    private fun navigateListFunc(listDataLambda: () -> Array<String>) = BaseClick<String> {
        logI("navigate to  vertical list")
        val destinations: Array<String> = listDataLambda.invoke()

        fun fragmentNavigator(dest: String) {
            when (dest) {
                // Rv
                "LinearRv" -> navigateToFragment(LinearRvFragment())
                "GridRv" -> navigateToFragment(GridRvFragment())
                "PagingRv" -> navigateToFragment(PagingRvFragment())

                // Remote
                "Remote Activity" -> startActivity(intentFor<RemoteActivity>())
                "Local Service" -> startService(intentFor<LocalService>())
                "Remote Service" -> startService(intentFor<RemoteService>())
                "Local ContentProvider" -> {
                    val uri = Uri.parse("content://me.yangxiaobin.local.authorities")
                    val cur: Cursor? = contentResolver.query(uri, null, null, null, null)
                    cur?.close()
                }
                "Remote ContentProvider" -> {
                    val uri = Uri.parse("content://me.yangxiaobin.remote.authorities")
                    val cur: Cursor? = contentResolver.query(uri, null, null, null, null)
                    cur?.close()
                }

                // Compose
                "MyBottomSheetDialogFragment" -> navigateToFragment(MyBottomSheetDialogFragment())

                else -> {
                    showToast("UnSupport key :$dest.")
                }
            }
        }

        val stringToClick: Map<String, () -> Unit> =
            destinations.associateWith { dest: String -> { fragmentNavigator(dest) } }

        this.navigateToFragment(ComposeVerticalListFragment(), "map" to stringToClick)
    }


    private val dataList: LinkedHashMap<String, BaseClick<String>> = linkedMapOf(

        "RecyclerView" to navigateListFunc {
            arrayOf(
                "LinearRv",
                "GridRv",
            )
        },

        "Multi Process" to navigateListFunc {
            arrayOf(
                "Remote Activity",
                "Local Service",
                "Remote Service",
                "Local ContentProvider",
                "Remote ContentProvider",
            )
        },

        "DI" to navigateListFunc {
            arrayOf(
                "Dagger2",
                "Dagger2.android",
                "Hilt",
            )
        },

        "Kotlin Jetpack Components" to navigateListFunc {
            arrayOf(
                "LifeCycle",
                "LiveData",
                "ViewModel",
                "Paging3",
                "Room",
                "Navigation",
            )
        },

        "Kotlin Jetpack Compose" to navigateListFunc {
            arrayOf(
                "MyBottomSheetDialogFragment",
            )
        },
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

        rv.adapter = SimpleRvAdapter<String>(
            dataList.keys.toList(),
            android.R.layout.simple_list_item_1
        ) { (vh: AbsVH, entity, pos: Int, _: MutableList<Any>) ->


            vh.requireView<TextView>(android.R.id.text1).run {

                @SuppressLint("SetTextI18n")
                text = "${pos + 1}. $entity"

                setOnClickListener { dataList.values.toList()[pos].click(entity ?: "") }
            }


        }

    }

    private fun Activity.navigateToFragment(target: Fragment, vararg pairs: Pair<String, Any?>) =
        supportFragmentManager.commit {
            target.arguments = bundleOf(*pairs)
            addToBackStack(null)
            add(R.id.content_main_activity, target)
        }

}
