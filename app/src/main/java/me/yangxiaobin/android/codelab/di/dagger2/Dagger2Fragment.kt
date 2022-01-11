package me.yangxiaobin.android.codelab.di.dagger2

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import me.yangxiaobin.android.kotlin.codelab.ext.showFragmentToast
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment

class Dagger2Fragment : AbsComposableFragment() {


    override val composableContent: @Composable () -> Unit = {

        Button(onClick = {
            showFragmentToast("Click the bt.")
        }) {

            Text(text = "Dagger2 Example.")
        }

    }
}
