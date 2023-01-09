package me.yangxiaobin.android.jank_sample

import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.uicontroller.showFragmentToast
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.ButtonsFragment
import me.yangxiaobin.logger.core.LogFacade

class NPEFragment : ButtonsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "NPEFragment"

    private var nullableStr:String? = null

    private var person:Person? = Person()


    override fun onClick(index: Int) {
        super.onClick(index)
        when (index) {

            0 -> {
                nullableStr!!.hashCode()
            }

            1->{
                person = null
                person!!.sayHi()
            }

            2->{
                showFragmentToast("click 2")
                JavaNPECreator().createNPE()
            }

            3->{
                showFragmentToast("click 3")
                JavaNPECreator().createNPE1()
            }

            else -> {

            }
        }
    }

    class Person{

        fun sayHi(){
        }
    }
}
