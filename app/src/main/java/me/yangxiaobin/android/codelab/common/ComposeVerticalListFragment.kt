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
import androidx.fragment.app.Fragment
import me.yangxiaobin.android.codelab.alerts.PopupWindowBtsFragment
import me.yangxiaobin.android.codelab.di.dagger2.Dagger2Fragment
import me.yangxiaobin.android.codelab.jepack_compose.CheckBoxFragment
import me.yangxiaobin.android.codelab.jepack_compose.MyBottomSheetDialogFragment
import me.yangxiaobin.android.codelab.jetpack_components.FlowFragment
import me.yangxiaobin.android.codelab.jetpack_components.MutableSharedFlowFragment
import me.yangxiaobin.android.codelab.log.LogTestFragment
import me.yangxiaobin.android.codelab.multi_process.LocalService
import me.yangxiaobin.android.codelab.multi_process.RemoteActivity
import me.yangxiaobin.android.codelab.multi_process.RemoteService
import me.yangxiaobin.android.codelab.multi_thread.FutureFragment
import me.yangxiaobin.android.codelab.multi_thread.ReentrantLockFragment
import me.yangxiaobin.android.codelab.multi_thread.ThreadFragment
import me.yangxiaobin.android.codelab.navigateToFragment
import me.yangxiaobin.android.codelab.qrcode.PermissionRequestFragment
import me.yangxiaobin.android.codelab.recyclerview.GridRvFragment
import me.yangxiaobin.android.codelab.recyclerview.LinearRvFragment
import me.yangxiaobin.android.codelab.recyclerview.PagingRvFragment
import me.yangxiaobin.android.codelab.retrofit.RetrofitFragment
import me.yangxiaobin.android.codelab.touch_event.ActionCancelEventFragment
import me.yangxiaobin.android.jank_sample.NPEFragment
import me.yangxiaobin.android.jank_sample.PerfettoSampleFragment
import me.yangxiaobin.android.kotlin.codelab.ext.showFragmentToast
import me.yangxiaobin.android.nav_lib.NavHostComposeFragment
import me.yangxiaobin.android.permission_example.PrivacyProtectionFragment
import me.yangxiaobin.android.proguard_lib.ReflectFragment
import me.yangxiaobin.android.webview.JsFunctionFragment
import me.yangxiaobin.android.webview.AbsWebViewFragment
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment
import me.yangxiaobin.qrcode.QrCodeScanActivity
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

        fun naviToFragment(f:Fragment) = ctx.navigateToFragment(f)

        when (dest) {
            // 1.Rv
            "LinearRv" -> naviToFragment(LinearRvFragment())
            "GridRv" -> naviToFragment(GridRvFragment())
            "PagingRv" -> naviToFragment(PagingRvFragment())


            // 2.Remote
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

            // 3.DI / dagger2
            "Dagger2" -> naviToFragment(Dagger2Fragment())


            // 4.Jetpack Components
            "MutableSharedFlow" -> naviToFragment(MutableSharedFlowFragment())
            "Flow" -> naviToFragment(FlowFragment())
            "Navigation" -> naviToFragment(NavHostComposeFragment())

            // 5.Kotlin Jetpack Compose
            "MyBottomSheetDialogFragment" -> naviToFragment(MyBottomSheetDialogFragment())
            "Compose CheckBox" -> naviToFragment(CheckBoxFragment())


            // 6.Retrofit Custom Converter.
            "CustomConverter" -> naviToFragment(RetrofitFragment())

            // 7.Multi Threads
            "Thread" -> naviToFragment(ThreadFragment())
            "ReentrantLock" -> naviToFragment(ReentrantLockFragment())
            "Future" -> naviToFragment(FutureFragment())

            // 8.Alerts
            "PopupWindow" -> naviToFragment(PopupWindowBtsFragment())

            // 9.Touch Events
            "ACTION_CANCEL" -> naviToFragment(ActionCancelEventFragment())

            // 10. QrScan
            "PermissionRequest" -> naviToFragment(PermissionRequestFragment())
            "QRCodeScanActivity" -> ctx.startActivity(ctx.intentFor<QrCodeScanActivity>())

            // 11. Proguard
            "Reflection" -> naviToFragment(ReflectFragment())

            // 12. permission
            "MIUI Privacy Protection" -> naviToFragment(PrivacyProtectionFragment())

            // 13. Jank Samples
            "Perfetto Sample" -> naviToFragment(PerfettoSampleFragment())
            "NPE" -> naviToFragment(NPEFragment())

            // 14. WebView
            "System WebView" -> naviToFragment(AbsWebViewFragment())
            "Js Function" -> naviToFragment(JsFunctionFragment())

            // 15. Log
            "LogTest" -> naviToFragment(LogTestFragment())

            else -> showFragmentToast("UnSupport key :$dest.")
        }
    }


}
