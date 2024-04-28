package me.yangxiaobin.android.kotlin.codelab.ext

import android.net.Uri


fun Uri.replaceUriParameter(key: String, newValue: String): Uri {
    val uri = this
    val params = uri.queryParameterNames
    val newUri = uri.buildUpon().clearQuery()
    for (param in params) {
        newUri.appendQueryParameter(
            param,
            if (param == key) newValue else uri.getQueryParameter(param)
        )
    }
    return newUri.build()
}


fun Uri.Builder.replaceUriParameter(key: String, newValue: String): Uri.Builder {
    val uri = this.build()

    val params = uri.queryParameterNames
    val newUriBuilder: Uri.Builder = uri.buildUpon().clearQuery()

    for (param in params) {
        newUriBuilder.appendQueryParameter(param, if (param == key) newValue else uri.getQueryParameter(param)
        )
    }
    return newUriBuilder
}
