package me.yangxiaobin.android.webview

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

/**
 * Android call Js ：1. webView.loadUrl ;
 *                  2. webView.evaluateJavascript
 *
 *
 * Js call Android ： 1.  webView addJavascriptInterface
 *                   2.  webView.shouldOverrideUrlLoading
 *                   3.  webView onJsAlert , onJsConfirm , onJsPrompt 拦截 js 消息
 *
 *  前端 WebView 指南之 Android 交互篇: https://75.team/post/android-webview-and-js
 */
class JsFunctionFragment : AbsWebViewFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "JsFunctionFragment"

    override val initialUrl: String = "file:///android_asset/web/test.html"

//    override val initialUrl: String = "https://zh.wikihow.com/%E7%94%A8HTML%E5%88%9B%E5%BB%BA%E4%B8%80%E4%B8%AA%E7%AE%80%E5%8D%95%E7%BD%91%E9%A1%B5"


    @SuppressLint("SetJavaScriptEnabled")
    override fun initWebView(webView: WebView) {
        super.initWebView(webView)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JsKit(logD), "myJs")
    }

    override fun onLoadUrlClick() {
        //super.onLoadUrlClick()

        //webView.loadUrl("javascript:jsMyFunction()")

        webView.evaluateJavascript("javascript:jsMyFunction()"
        ) {
            logD("evaluateJavascript, valueCallback , res :$it.")
        }
    }

    class JsKit(private val log: (message: String) -> Unit) {

        @JavascriptInterface
        fun hello() {
            log("hello from android side.")
        }
    }
}
