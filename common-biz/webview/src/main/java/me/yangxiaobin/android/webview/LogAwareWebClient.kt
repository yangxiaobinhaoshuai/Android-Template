package me.yangxiaobin.android.webview

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Message
import android.view.KeyEvent
import android.webkit.*

open class LogAwareWebClient(private val log: (message: String) -> Unit) : WebViewClient() {

    private fun logD(message: String) = log("WebClient: $message")

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return super.shouldOverrideUrlLoading(view, request)
            .also { logD("shouldOverrideUrlLoading, view:${view.hashCode()}, url:${request?.url?.toString()}.") }
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        logD("onPageStarted, view:${view?.hashCode()}, url:$url, favicon:$favicon.")
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        logD("onPageFinished, view:${view.hashCode()}, url:$url.")
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)
        logD("onLoadResource, view:${view?.hashCode()}, url:$url.")
    }

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        super.onPageCommitVisible(view, url)
        logD("onPageCommitVisible, view:${view.hashCode()}, url:$url.")
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        return super.shouldInterceptRequest(view, request)
            .also { logD("shouldInterceptRequest, view:${view.hashCode()}, request:${request}.") }
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        logD("onReceivedError, view:${view.hashCode()}, request:${request}, error:$error.")
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
        logD("onReceivedHttpError, view:${view.hashCode()}, request:$request, errorRequest:$errorResponse.")
    }

    override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
        super.onFormResubmission(view, dontResend, resend)
        logD("onFormResubmission, view:${view.hashCode()}, dontResend:$dontResend, resend:$resend.")
    }

    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
        super.doUpdateVisitedHistory(view, url, isReload)
        logD("doUpdateVisitedHistory, view:${view.hashCode()}, url:$url, isReload:$isReload.")
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        super.onReceivedSslError(view, handler, error)
        logD("onReceivedSslError, view:${view.hashCode()}, handler:$handler, error:$error.")
    }

    override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
        super.onReceivedClientCertRequest(view, request)
        logD("onReceivedClientCertRequest, view:${view.hashCode()}, request:$request.")
    }

    override fun onReceivedHttpAuthRequest(
        view: WebView?,
        handler: HttpAuthHandler?,
        host: String?,
        realm: String?
    ) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm)
        logD("onReceivedHttpAuthRequest, view:${view.hashCode()}, handler:$handler, host:$host, realm:$realm.")
    }

    override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
        return super.shouldOverrideKeyEvent(view, event)
            .also { logD("shouldOverrideKeyEvent, view:${view.hashCode()}, event:$event") }
    }

    override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
        super.onUnhandledKeyEvent(view, event)
        logD("onUnhandledKeyEvent, view:${view.hashCode()}, event:$event.")
    }

    override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
        super.onScaleChanged(view, oldScale, newScale)
        logD("onScaleChanged, view:${view.hashCode()}, oldScale:$oldScale, newScale:$newScale.")
    }

    override fun onReceivedLoginRequest(
        view: WebView?,
        realm: String?,
        account: String?,
        args: String?
    ) {
        super.onReceivedLoginRequest(view, realm, account, args)
        logD("onReceivedLoginRequest, view:${view.hashCode()}, realm:$realm, account:$account, args:$args.")
    }

    override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
        return super.onRenderProcessGone(view, detail)
            .also { logD("onRenderProcessGone, view:${view.hashCode()}, detail:$detail.") }
    }

    override fun onSafeBrowsingHit(
        view: WebView?,
        request: WebResourceRequest?,
        threatType: Int,
        callback: SafeBrowsingResponse?
    ) {
        super.onSafeBrowsingHit(view, request, threatType, callback)
        logD("onSafeBrowsingHit, view:${view.hashCode()}, request:$request, threatType:$threatType, callback:$callback.")
    }
}
