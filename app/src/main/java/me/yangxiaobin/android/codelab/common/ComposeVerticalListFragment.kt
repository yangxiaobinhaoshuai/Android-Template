package me.yangxiaobin.android.codelab.common

import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import me.yangxiaobin.kotlin.codelab.ext.getByIndex
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment

class ComposeVerticalListFragment : AbsComposableFragment() {

    private val options: LinkedHashMap<String, Function0<Unit>> by lazy {
        @Suppress("UNCHECKED_CAST")
        arguments?.getSerializable("map") as? LinkedHashMap<String, Function0<Unit>>
            ?: LinkedHashMap()
    }

    private val argsLiveData: MutableLiveData<LinkedHashMap<String, Function0<Unit>>> =
        MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logI("getOptions :$options")
        argsLiveData.value = options
    }

    override val composableContent: @Composable () -> Unit = {
        val stateValue: LinkedHashMap<String, Function0<Unit>> =
            argsLiveData.observeAsState(LinkedHashMap()).value
        ComposeList(stateValue)
    }

    @Composable
    private fun ComposeList(args: LinkedHashMap<String, Function0<Unit>>) = LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(20.dp)
    ) {

        items(args.size) { index: Int ->
            val (itemString, onClick) = args.getByIndex(index)

            ListItem("${index + 1}. $itemString", onClick)

        }
    }

    @Composable
    private fun ListItem(itemString: String, onClick: Function0<Unit>) = Column(
        modifier = Modifier
            .height(50.dp)
            .clickable { onClick.invoke() }
    ) {

        Box(Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
            Text(text = itemString)
        }

        Divider(color = androidx.compose.ui.graphics.Color.Black)
    }

}
