package me.yangxiaobin.android.kotlin.codelab.base

import androidx.lifecycle.ViewModel
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import java.io.Closeable

open class AbsViewModel : ViewModel(), LogAbility {

    override fun addCloseable(closeable: Closeable) {
        super.addCloseable(closeable)
        logD("addCloseable: $closeable")
    }

    override fun onCleared() {
        super.onCleared()
        logD("onCleared")
    }
}