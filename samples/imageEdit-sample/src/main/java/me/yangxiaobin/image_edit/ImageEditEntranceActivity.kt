package me.yangxiaobin.image_edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.yangxiaobin.kotlin.compose.lib.AbsComposableActivity
import org.jetbrains.anko.intentFor

class ImageEditEntranceActivity : AbsComposableActivity() {

    override val composableContent: @Composable () -> Unit = {
        Surface(modifier = Modifier, color = Color.Cyan) {

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(fontSize = 20.sp, text = "ImageEditActivity")

                Text(
                    modifier = Modifier
                        .padding(vertical = 30.dp)
                        .size(width = 100.dp, 40.dp)
                        .clickable { startActivity(intentFor<ImageEditActivity>()) },
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    text = "Bt1"
                )

                Text(
                    modifier = Modifier
                        .padding(vertical = 30.dp)
                        .size(width = 100.dp, 40.dp)
                        .clickable { },
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    text = "Bt2"
                )

                Text(
                    modifier = Modifier
                        .padding(vertical = 30.dp)
                        .size(width = 100.dp, 40.dp)
                        .clickable { },
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    text = "Bt2"
                )
            }
        }
    }


}