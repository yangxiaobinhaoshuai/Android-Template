package me.yangxiaobin.kotlin.compose.lib.widget

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale


@Composable
fun SimpleImage(
    painter: Painter,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
) {
    Image(
        modifier = modifier,
        painter = painter,
        alignment = alignment,
        contentScale = contentScale,
        contentDescription = null,
    )
}
