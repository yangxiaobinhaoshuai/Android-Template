package me.yangxiaobin.android.kotlin.codelab.base

import android.content.Context
import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.table
import me.yangxiaobin.android.kotlin.codelab.ext.currentProcessName
import me.yangxiaobin.android.kotlin.codelab.ext.getCurrentPid
import me.yangxiaobin.android.kotlin.codelab.ext.getCurrentProcessName
import me.yangxiaobin.android.kotlin.codelab.ext.simpleName
import me.yangxiaobin.android.kotlin.codelab.log.L
import me.yangxiaobin.kotlin.codelab.log.ILog
import me.yangxiaobin.kotlin.codelab.log.logD
import me.yangxiaobin.kotlin.codelab.log.logE
import me.yangxiaobin.kotlin.codelab.log.logI


/**
 * Used in derived context classes.
 */
internal val Context?.getLogSuffix get() = "${this.simpleName}(hash:${this.hashCode()}, tName:${Thread.currentThread().name}, tid:${Thread.currentThread().id}, pName:${this?.getCurrentProcessName}, pid:$getCurrentPid)"

interface LogAbility {

    val LogAbility.TAG: String
        get() = "LogAbility:${this@LogAbility.simpleName.take(11)}"

    val logger: ILog get() = L.copy()
    val logI: (message: String) -> Unit get() = logger.logI(TAG)
    val logD: (message: String) -> Unit get() = logger.logD(this@LogAbility.TAG)
    val logE: (message: String) -> Unit get() = logger.logE(this@LogAbility.TAG)

    fun getTable(tag: String, message: String): String = table {
        cellStyle {
            // These options affect the style of all cells contained within the table.
            border = true
        }
        // MainActivity(hash:209653499, tName:main, tid:2, pName:Main(me.yangxiaobin.android.codelab), pid:16925)
        row {
            cell("tag") {
                alignment = TextAlignment.MiddleCenter
            }
            cell("class") {
                alignment = TextAlignment.MiddleCenter
            }
            cell("hash") {
                alignment = TextAlignment.MiddleCenter
            }
            cell("t") {
                alignment = TextAlignment.MiddleCenter
            }
            cell("tid") {
                alignment = TextAlignment.MiddleCenter
            }
            cell("pid") {
                alignment = TextAlignment.MiddleCenter
            }
        }
        row {
            cell(tag) {
                alignment = TextAlignment.MiddleCenter
            }
            cell(this@LogAbility.simpleName) {
                alignment = TextAlignment.MiddleCenter
            }
            cell(this.hashCode()) {
                alignment = TextAlignment.MiddleCenter
            }
            cell(Thread.currentThread().name) {
                alignment = TextAlignment.MiddleCenter
            }
            cell(Thread.currentThread().id) {
                alignment = TextAlignment.MiddleCenter
            }
            cell(getCurrentPid) {
                alignment = TextAlignment.MiddleCenter
            }
        }

        row {
            cell("p : $currentProcessName") {
                columnSpan = 6
            }
        }
        row {
            cell(message) {
                columnSpan = 6
            }
        }
    }.toString()


}
