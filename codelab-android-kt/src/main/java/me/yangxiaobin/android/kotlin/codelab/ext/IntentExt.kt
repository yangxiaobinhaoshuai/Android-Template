package me.yangxiaobin.android.kotlin.codelab.ext

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable


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


/**
 * 创建 Intent 并支持传递参数
 */
inline fun <reified T : Context> Context.intentFor(
    vararg params: Pair<String, Any?>
): Intent {
    return Intent(this, T::class.java).apply {
        params.forEach { (key, value) ->
            when (value) {
                is String -> putExtra(key, value)
                is Int -> putExtra(key, value)
                is Long -> putExtra(key, value)
                is Boolean -> putExtra(key, value)
                is Float -> putExtra(key, value)
                is Double -> putExtra(key, value)
                is Serializable -> putExtra(key, value)
                is Parcelable -> putExtra(key, value)
                else -> throw IllegalArgumentException("Unsupported type: ${value?.javaClass}")
            }
        }
    }
}
