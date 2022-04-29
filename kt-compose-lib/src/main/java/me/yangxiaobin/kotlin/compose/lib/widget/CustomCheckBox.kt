package me.yangxiaobin.kotlin.compose.lib.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import me.yangxiaobin.kotlin.compose.lib.R


@Composable
fun CustomCheckBox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    normalDrawableId: Int = R.drawable.eed_common_base_line_xviii_radiobox_18_normal,
    selectDrawableId: Int = R.drawable.eed_common_base_area_xviii_radiobox_18_selected,
    onCheckedChange: ((Boolean) -> Unit)? = null,
) {
    Box(
        modifier.clickable {
            if (onCheckedChange != null) {
                onCheckedChange(!checked)
            }
        }
    ) {

        // normal
        if (!checked)
            Image(
                painter = painterResource(id = normalDrawableId),
                contentDescription = null,
            )

        // select
        if (checked)
            Image(
                painter = painterResource(id = selectDrawableId),
                contentDescription = null,
            )

    }
}
