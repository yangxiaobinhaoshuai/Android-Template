package me.yangxiaobin.android.kotlin.codelab.base

import android.content.Context
import me.yangxiaobin.android.kotlin.codelab.ext.getCurrentPid
import me.yangxiaobin.android.kotlin.codelab.ext.getCurrentProcessName
import me.yangxiaobin.android.kotlin.codelab.ext.simpleName
import me.yangxiaobin.android.kotlin.codelab.log.L
import me.yangxiaobin.kotlin.codelab.log.ILog
import me.yangxiaobin.kotlin.codelab.log.logD
import me.yangxiaobin.kotlin.codelab.log.logE
import me.yangxiaobin.kotlin.codelab.log.logI


internal val Context.getLogPrefix get() = "${this.simpleName}(hash:${this.hashCode()}, pName:${this.getCurrentProcessName}, pid:$getCurrentPid)"

interface LogAbility {

    open val LogAbility.TAG: String
        get() = "Ability:${this@LogAbility.javaClass.simpleName.take(11)}"

    val logger: ILog get() = L.copy()
    val logI: (message: String) -> Unit get() = logger.logI(TAG)
    val logD: (message: String) -> Unit get() = logger.logD(this@LogAbility.TAG)
    val logE: (message: String) -> Unit get() = logger.logE(this@LogAbility.TAG)

}
