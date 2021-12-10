package me.yangxiaobin.android.codelab.compose

import android.graphics.Color
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.ext.Action
import me.yangxiaobin.android.kotlin.codelab.ext.MatchParentParams
import me.yangxiaobin.android.kotlin.codelab.ext.TypedAction

class ComposeFragment : AbsFragment() {

    private val options = listOf(
        "1.",
        "2.",
        "3.",
        "4.",
        "5.",
        "6.",
    )

    override fun createRootView(): View = ComposeView(requireContext())
        .apply {
            this.layoutParams = MatchParentParams
            this.setBackgroundColor(Color.WHITE)
            this.isClickable = true
            setContent {
                MaterialTheme {
                    ComposeList(onListItemClick)
                }
            }
        }


    @Composable
    private fun ComposeList(onClick: TypedAction<Int> = {}) = LazyColumn(
        modifier = Modifier.padding(vertical = 10.dp),
        contentPadding = PaddingValues(20.dp)
    ) {

        items(options.size) { index ->

            ListItem(index, onClick)

        }
    }

    @Composable
    private fun ListItem(index: Int, onClick: TypedAction<Int>) = Column(
        modifier = Modifier
            .height(50.dp)
            .clickable { onClick(index) }
    ) {
        Text(text = options[index])

        Divider(color = androidx.compose.ui.graphics.Color.Black)
    }


    private val onListItemClick = { index: Int ->
        logI("on compose list item click, item :$index")
    }

}
