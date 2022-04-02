package me.yangxiaobin.android.codelab.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment

private const val COUNT = 5

open class ButtonsFragment : AbsComposableFragment() {


    override val composableContent = @Composable {


        Column(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            repeat(COUNT) { index ->

                Button(
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .size(width = 140.dp, height = 60.dp),
                    onClick = { onClick(index) }
                ) {

                    Text(
                        modifier = Modifier,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = "Button $index"
                    )

                }

            }
        }

    }

    protected open fun onClick(index: Int) = when (index) {
        0 -> {}
        1 -> Unit
        2 -> Unit
        3 -> Unit
        4 -> Unit
        else -> Unit
    }


}
