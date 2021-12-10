package me.yangxiaobin.android.codelab

import android.database.Cursor
import android.net.Uri
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import me.yangxiaobin.android.codelab.compose.ComposableFragment
import me.yangxiaobin.android.codelab.compose.MyBottomSheetDialogFragment
import me.yangxiaobin.android.codelab.multi_process.LocalService
import me.yangxiaobin.android.codelab.multi_process.RemoteActivity
import me.yangxiaobin.android.codelab.multi_process.RemoteService
import me.yangxiaobin.android.codelab.recyclerview.GridRvFragment
import me.yangxiaobin.android.codelab.recyclerview.LinearRvFragment
import me.yangxiaobin.android.codelab.recyclerview.PagingRvFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.base.AbsActivity
import me.yangxiaobin.android.kotlin.codelab.ext.setOnItemClickListener
import me.yangxiaobin.android.kotlin.codelab.ext.setSimpleDivider
import me.yangxiaobin.android.kotlin.codelab.recyclerview.SimpleRvAdapter
import org.jetbrains.anko.intentFor

class MainActivity : AbsActivity() {

    private val dataList = listOf(
        "0.LinearRv",
        "1.GridRv",
        "2.PagingRv",
        "3.startRemoteActivity",
        "4.StartLocalService",
        "5.StartRemoteService",
        "6.AccessLocalProvider",
        "7.AccessRemoteProvider",
        "8.ComposeBottomDialogFragment",
        "9.ComposeBottomDialogUIComponent",
        )

    override val LogAbility.TAG: String get() = "Sample-app"


    override val contentResId: Int
        get() = R.layout.activity_main

    override val handleBackPress: Boolean
        get() = this.isTaskRoot

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

        rv.adapter =
            SimpleRvAdapter<String>(dataList, android.R.layout.simple_list_item_1) { (vh, pos, _) ->
                vh.requireView<TextView>(android.R.id.text1).text = dataList[pos]
            }

        rv.setOnItemClickListener {
            logI("rv onItem click :$it")

            fun navigateFragment(target: Fragment) = supportFragmentManager.commit {
                addToBackStack(null)
                add(R.id.content_main_activity, target)
            }

            when (it.second) {
                0 -> navigateFragment(LinearRvFragment())
                1 -> navigateFragment(GridRvFragment())
                2 -> navigateFragment(PagingRvFragment())

                // start remote activity
                3 -> startActivity(intentFor<RemoteActivity>())

                // start local service
                4 -> startService(intentFor<LocalService>())

                // start remote service
                5 -> startService(intentFor<RemoteService>())

                6 -> {
                    // start local content Provider
                    val uri = Uri.parse("content://me.yangxiaobin.local.authorities")
                    val cur: Cursor? = contentResolver.query(uri, null, null, null, null)
                    cur?.close()
                }

                7 -> {
                    // start remote content Provider
                    val uri = Uri.parse("content://me.yangxiaobin.remote.authorities")
                    val cur: Cursor? = contentResolver.query(uri, null, null, null, null)
                    cur?.close()
                }

                8 -> MyBottomSheetDialogFragment().show(supportFragmentManager,null)
                9 -> navigateFragment(ComposableFragment())
            }
        }

    }

}
