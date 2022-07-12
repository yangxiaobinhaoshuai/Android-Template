package me.yangxiaobin.android.webview

import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.MatchParentParams
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment
import me.yangxiaobin.logger.core.LogFacade

private const val BAIDU = "https://www.baidu.com/"
private const val JUEJIN = "https://juejin.cn/post/6844903591245774862"

class WebViewFragment : AbsComposableFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "WebViewFragment"

    private val urlState: MutableState<String> = mutableStateOf(BAIDU)

    override val composableContent: @Composable () -> Unit = {

        Column(modifier = Modifier.padding(5.dp)) {

            Button(
                modifier = Modifier,
                onClick = {
                    urlState.value = JUEJIN
                }
            ) {

                Text(text = "Load url")

            }

            AndroidWebView()

        }

    }

    @Composable
    private fun AndroidWebView() {
        AndroidView(
            factory = { context: Context ->

                // CheckPermission, permission :android.permission.INTERNET
                WebView(context).apply {
                    this.layoutParams = MatchParentParams
                    this.initialize()
                    this.loadUrl(urlState.value)
                }

            },
            update = {
                it.loadUrl(urlState.value)
            }
        )
    }

    private fun WebView.initialize() {
        // 在本应用展示网页
        this.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                logD("shouldOverrideUrlLoading, view:$view, request:$request.")
                view.loadUrl(request.url.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

    }
}
