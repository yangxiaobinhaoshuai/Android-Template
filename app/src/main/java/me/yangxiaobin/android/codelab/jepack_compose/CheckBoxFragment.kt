package me.yangxiaobin.android.codelab.jepack_compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.toColor
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment
import me.yangxiaobin.kotlin.compose.lib.widget.CustomCheckBox
import me.yangxiaobin.logger.core.LogFacade

class CheckBoxFragment : AbsComposableFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "CheckBoxFragment"


    override val composableContent: @Composable () -> Unit = @Composable {

        val isChecked = remember { mutableStateOf(false) }

        Column() {

//            Checkbox(
//                modifier = Modifier
//                    .padding(30.dp)
//                    .height(20.dp)
//                    .width(20.dp)
//                    .background(color = Color(HexColors.YELLOW_A200.toColor)),
//                checked = isChecked.value,
//                onCheckedChange = { isChecked.value = it },
//            )

            CustomCheckBox(
                modifier = Modifier
                    .padding(30.dp)
                    .height(20.dp)
                    .width(20.dp),
                checked = isChecked.value,
                onCheckedChange = { isChecked.value = it },
            )

        }
    }


}
