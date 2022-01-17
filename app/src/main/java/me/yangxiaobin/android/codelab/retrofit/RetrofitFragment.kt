package me.yangxiaobin.android.codelab.retrofit

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment

class RetrofitFragment : AbsComposableFragment() {

    override val composableContent: @Composable () -> Unit = {

        Text(text = "Retrofit.")

    }

}
