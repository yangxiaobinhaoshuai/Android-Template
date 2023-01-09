package me.yangxiaobin.android.kotlin.codelab.base.ability

import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.table
//import me.yangxiaobin.android.kotlin.codelab.ext.appinfo.getCurrentProcessName
//import me.yangxiaobin.android.kotlin.codelab.ext.appinfo.getGetCurrentPid
import me.yangxiaobin.android.kotlin.codelab.log.L
import me.yangxiaobin.kotlin.codelab.ext.neatName
import me.yangxiaobin.logger.core.LogFacade
import me.yangxiaobin.logger.core.LogLevel
import me.yangxiaobin.logger.log


interface LogAbility {

    val LogAbility.TAG: String get() = "LogAbility:${this@LogAbility.neatName.take(11)}"

    /**
     * Must be getter, or u will get 'java.lang.NullPointerException: Parameter specified as non-null is null: method kotlin.jvm.internal.Intrinsics.checkNotNullParameter, parameter <this>'
     *
     * trigger this leaking.
     */
    val logger: LogFacade get() = L.clone()

    val logI: (message: String) -> Unit get() = logger.log(LogLevel.INFO, TAG)
    val logD: (message: String) -> Unit get() = logger.log(LogLevel.DEBUG, TAG)
    val logE: (message: String) -> Unit get() = logger.log(LogLevel.ERROR, TAG)

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
            cell(this@LogAbility.neatName) {
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
//            cell(getCurrentPid) {
//                alignment = TextAlignment.MiddleCenter
//            }
        }

//        row {
//            cell("p : $currentProcessName") {
//                columnSpan = 6
//            }
//        }
        row {
            cell(message) {
                columnSpan = 6
            }
        }
    }.toString()


}
