package me.yangxiaobin.android.embedding_compat

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.uicontroller.showFragmentToast
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyFragment
import me.yangxiaobin.logger.core.LogFacade

class CharacterSelectionFragment : EmptyFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "CharacterSelectionFragment"


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
    }


    override fun onButtonClick(index: Int, v: View) {
        super.onButtonClick(index, v)
        when (index) {
            0 -> {
                showFragmentToast("Character!!")
            }
            1 -> {}
            else -> {}
        }
    }

}
