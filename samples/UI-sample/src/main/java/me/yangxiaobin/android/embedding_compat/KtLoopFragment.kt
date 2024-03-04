package me.yangxiaobin.android.embedding_compat

import android.view.View
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyFragment
import me.yangxiaobin.logger.core.LogFacade

class KtLoopFragment : EmptyFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "KtLoopFragment"


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
    }

    override fun onButtonClick(index: Int, v: View) {
        super.onButtonClick(index, v)
        when (index) {
            0 -> {
                listOf(1, 2, 3, 4, 5, 6, 7).forEach {

                    if (it == 3) {
                        return@forEach
                    }

                    logD(" for each : $it.")
                }


            }

            1 -> {

                loop@ for (i in 1..10) {
                    if (i == 3) break@loop
                    if (i == 2) continue@loop
                    logD(" for loop :$i.")
                }
            }

            2 -> {
                run lit@ {
                    listOf(1, 2, 3, 4, 5).forEach {
                        if (it == 3) return@lit // local return to the caller of the lambda, i.e. the forEach loop
                        logD(" run block for each: $it.")
                    }
                    logD(" done with explicit label")
                }

            }

            else -> {}
        }
    }


}
