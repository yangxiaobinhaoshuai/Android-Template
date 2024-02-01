package me.yangxiaobin.android.kotlin.codelab.ext

import android.content.Intent
import android.os.Bundle
import java.lang.StringBuilder


fun Intent.dumpIntentExtras(): String {
    val sb = StringBuilder()
    val bundle: Bundle? = this.extras
    if (bundle != null) {
        for (key in bundle.keySet()) {
            sb.append("($key - ${if (bundle.get(key) != null) bundle.get(key) else "NULL"})")
        }
    }
    return sb.toString()
}
