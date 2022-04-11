package me.yangxiaobin.android.codelab.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment


open class ButtonsFragment : AbsComposableFragment() {

    protected open val buttonCount = 8

    protected val textFlow = mutableStateOf("")


    override val composableContent = @Composable {

        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(vertical = 40.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(text = textFlow.value)

            repeat(buttonCount) { index ->

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
