package me.yangxiaobin.android.webview

import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.view.View
import android.webkit.*

open class LogAwareWebChromeClient(private val log: (message: String) -> Unit) : WebChromeClient() {

    private fun logD(message: String) = log("ChromeClient: $message")

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        logD("onProgressChanged, view:${view.hashCode()}, newProgress:$newProgress.")
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        logD("onReceivedTitle, view:${view.hashCode()}, title:$title.")
    }

    override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
        super.onReceivedIcon(view, icon)
        logD("onReceivedIcon, view:${view.hashCode()}, icon:$icon.")
    }

    override fun onReceivedTouchIconUrl(view: WebView?, url: String?, precomposed: Boolean) {
        super.onReceivedTouchIconUrl(view, url, precomposed)
        logD("onReceivedTouchIconUrl, view:${view.hashCode()}, url:$url, precomposed:$precomposed.")
    }

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        super.onShowCustomView(view, callback)
        logD("onShowCustomView, view:${view.hashCode()}, callback:$callback.")
    }


    override fun onHideCustomView() {
        super.onHideCustomView()
        logD("onHideCustomView.")
    }

    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
            .also { logD("onCreateWindow, view:${view.hashCode()}, isDialog:$isDialog, isUserGesture:$isUserGesture, resultMsg:$resultMsg.") }
    }

    override fun onRequestFocus(view: WebView?) {
        super.onRequestFocus(view).also { logD("onRequestFocus, view:${view.hashCode()}.") }
    }

    override fun onCloseWindow(window: WebView?) {
        super.onCloseWindow(window).also { logD("onCloseWindow, window:$window.") }
    }

    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        return super.onJsAlert(view, url, message, result)
            .also { logD("onJsAlert, view:${view.hashCode()}, url:$url, message:$message, result:$result.") }
    }

    override fun onJsConfirm(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        return super.onJsConfirm(view, url, message, result)
            .also { logD("onJsConfirm, view:${view.hashCode()}, url:$url, message:$message, result:$result.") }
    }

    override fun onJsPrompt(
        view: WebView?,
        url: String?,
        message: String?,
        defaultValue: String?,
        result: JsPromptResult?
    ): Boolean {
        return super.onJsPrompt(view, url, message, defaultValue, result)
            .also { logD("onJsPrompt, view:${view.hashCode()}, url:$url, message:$message, defaultValue:$defaultValue, result:$result.") }
    }

    override fun onJsBeforeUnload(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        return super.onJsBeforeUnload(view, url, message, result)
            .also { logD("onJsBeforeUnload, view:${view.hashCode()}, url:$url, message:$message, result:$result.") }
    }

    override fun onGeolocationPermissionsShowPrompt(
        origin: String?,
        callback: GeolocationPermissions.Callback?
    ) {
        super.onGeolocationPermissionsShowPrompt(origin, callback)
        logD("onGeolocationPermissionsShowPrompt, origin:$origin, callback:$callback.")
    }

    override fun onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt()
        logD("onGeolocationPermissionsHidePrompt.")
    }

    override fun onPermissionRequest(request: PermissionRequest?) {
        super.onPermissionRequest(request)
        logD("onPermissionRequest, request:$request.")
    }

    override fun onPermissionRequestCanceled(request: PermissionRequest?) {
        super.onPermissionRequestCanceled(request)
        logD("onPermissionRequestCanceled, request:$request.")
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        return super.onConsoleMessage(consoleMessage)
            .also { logD("onConsoleMessage, consoleMessage:${consoleMessage?.message()}.") }
    }

    override fun getDefaultVideoPoster(): Bitmap? {
        return super.getDefaultVideoPoster().also { logD("getDefaultVideoPoster.") }
    }

    override fun getVideoLoadingProgressView(): View? {
        return super.getVideoLoadingProgressView().also { logD("getVideoLoadingProgressView.") }
    }

    override fun getVisitedHistory(callback: ValueCallback<Array<String>>?) {
        super.getVisitedHistory(callback)
        logD("getVisitedHistory, callback:$callback.")
    }

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
            .also { logD("onShowFileChooser,webView:${webView.hashCode()}, filePathCallback:$filePathCallback, fileChooserParams:$fileChooserParams.") }
    }
}
