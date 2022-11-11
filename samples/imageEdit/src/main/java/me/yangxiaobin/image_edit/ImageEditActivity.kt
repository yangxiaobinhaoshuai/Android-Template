package me.yangxiaobin.image_edit

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import me.yangxiaobin.kotlin.compose.lib.AbsComposableActivity

class ImageEditActivity : AbsComposableActivity() {

    override val composableContent: @Composable () -> Unit = {
            Surface(modifier = Modifier,color = Color.Cyan) {

                Text("ImageEditActivity")
            }
    }

}