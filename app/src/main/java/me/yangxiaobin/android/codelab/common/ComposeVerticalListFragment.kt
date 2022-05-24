package me.yangxiaobin.android.codelab.common

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.yangxiaobin.android.codelab.alerts.PopupWindowBtsFragment
import me.yangxiaobin.android.codelab.di.dagger2.Dagger2Fragment
import me.yangxiaobin.android.codelab.jepack_compose.CheckBoxFragment
import me.yangxiaobin.android.codelab.jepack_compose.MyBottomSheetDialogFragment
import me.yangxiaobin.android.codelab.jetpack_components.FlowFragment
import me.yangxiaobin.android.codelab.jetpack_components.MutableSharedFlowFragment
import me.yangxiaobin.android.codelab.multi_process.LocalService
import me.yangxiaobin.android.codelab.multi_process.RemoteActivity
import me.yangxiaobin.android.codelab.multi_process.RemoteService
import me.yangxiaobin.android.codelab.multi_thread.FutureFragment
import me.yangxiaobin.android.codelab.multi_thread.ReentrantLockFragment
import me.yangxiaobin.android.codelab.multi_thread.ThreadFragment
import me.yangxiaobin.android.codelab.navigateToFragment
import me.yangxiaobin.android.codelab.recyclerview.GridRvFragment
import me.yangxiaobin.android.codelab.recyclerview.LinearRvFragment
import me.yangxiaobin.android.codelab.recyclerview.PagingRvFragment
import me.yangxiaobin.android.codelab.retrofit.RetrofitFragment
import me.yangxiaobin.android.codelab.touch_event.ActionCancelEventFragment
import me.yangxiaobin.android.kotlin.codelab.ext.showContextToast
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment
import org.jetbrains.anko.intentFor

/**
 * 二级子菜单 Compose 列表
 */
class ComposeVerticalListFragment : AbsComposableFragment() {

    private val subMenus: Array<String> by lazy {
        @Suppress("UNCHECKED_CAST")
        arguments?.getSerializable("subMenus") as? Array<String> ?: emptyArray()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logI("getOptions :$subMenus.")
    }

    override val composableContent: @Composable () -> Unit = {
        ComposeList(subMenus)
    }

    @Composable
    private fun ComposeList(subMenus: Array<String>) = LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(20.dp)
    ) {

        items(subMenus.size) { index: Int ->

            ListItem(index,subMenus[index])

        }
    }

    @Composable
    private fun ListItem(index: Int, originalName: String) = Column(
        modifier = Modifier
            .height(50.dp)
            .clickable { fragmentNavigator(originalName) }
    ) {

        Box(Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
            Text(text = "${index + 1}. $originalName")
        }

        Divider(color = androidx.compose.ui.graphics.Color.Black)
    }

    private fun fragmentNavigator(dest: String) {

        val ctx = requireActivity()

        when (dest) {
            // Rv
            "LinearRv" -> ctx.navigateToFragment(LinearRvFragment())
            "GridRv" -> ctx.navigateToFragment(GridRvFragment())
            "PagingRv" -> ctx.navigateToFragment(PagingRvFragment())


            // Remote
            "Remote Activity" -> ctx.startActivity(ctx.intentFor<RemoteActivity>())
            "Local Service" -> ctx.startService(ctx.intentFor<LocalService>())
            "Remote Service" -> ctx.startService(ctx.intentFor<RemoteService>())
            "Local ContentProvider" -> {
                val uri = Uri.parse("content://me.yangxiaobin.local.authorities")
                val cur: Cursor? = ctx.contentResolver.query(uri, null, null, null, null)
                cur?.close()
            }
            "Remote ContentProvider" -> {
                val uri = Uri.parse("content://me.yangxiaobin.remote.authorities")
                val cur: Cursor? = ctx.contentResolver.query(uri, null, null, null, null)
                cur?.close()
            }


            // Jetpack Components
            "MutableSharedFlow" -> ctx.navigateToFragment(MutableSharedFlowFragment())
            "Flow" -> ctx.navigateToFragment(FlowFragment())


            // Kotlin Jetpack Compose
            "MyBottomSheetDialogFragment" -> ctx.navigateToFragment(MyBottomSheetDialogFragment())
            "Compose CheckBox" -> ctx.navigateToFragment(CheckBoxFragment())


            // DI / dagger2
            "Dagger2" -> ctx.navigateToFragment(Dagger2Fragment())


            // Retrofit Custom Converter.
            "CustomConverter" -> ctx.navigateToFragment(RetrofitFragment())

            // Multi Threads
            "Thread" -> ctx.navigateToFragment(ThreadFragment())
            "ReentrantLock" -> ctx.navigateToFragment(ReentrantLockFragment())
            "Future" -> ctx.navigateToFragment(FutureFragment())

            // Alerts
            "PopupWindow" -> ctx.navigateToFragment(PopupWindowBtsFragment())

            // Touch Events
            "ACTION_CANCEL" -> ctx.navigateToFragment(ActionCancelEventFragment())

            else -> ctx.showContextToast("UnSupport key :$dest.")
        }
    }


}
