package me.yangxiaobin.android.codelab.compose

import android.graphics.Color
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility

@ExperimentalMaterialApi
class ComposableFragment: AbsFragment() {

    override val LogAbility.TAG: String get() = "ComposableUI"


    override fun createRootView(): View = ComposeView(requireContext())
        .apply {
            this.setBackgroundColor(Color.YELLOW)
            this.setContent{
                HomeScreen()
            }
        }

    /**
     * see https://proandroiddev.com/bottom-sheet-in-jetpack-compose-c8ab7a297aac
     */
    @ExperimentalMaterialApi
    @Composable
    fun HomeScreen() {
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
        )
        val coroutineScope = rememberCoroutineScope()
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Text(text = "Hello from sheet")
                }
            }, sheetPeekHeight = 56.dp
        ) {
            Button(onClick = {
                coroutineScope.launch {

                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    } else {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            }) {
                Text(text = "Expand/Collapse Bottom Sheet")
            }
        }
    }

}
