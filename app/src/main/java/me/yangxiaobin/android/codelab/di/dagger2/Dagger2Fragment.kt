package me.yangxiaobin.android.codelab.di.dagger2

import android.os.Bundle
import android.view.View
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import me.yangxiaobin.android.kotlin.codelab.ext.showFragmentToast
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment
import javax.inject.Inject

class Dagger2Fragment : AbsComposableFragment() {

    @Inject
    lateinit var stuffManager: StuffManager


    override val composableContent: @Composable () -> Unit = {

        Button(onClick = {
            showFragmentToast("Click the bt.")
        }) {

            Text(text = "Dagger2 Example.")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DaggerStuffComponent.create().injectDagger2Fragment(this)
        stuffManager.work()
    }
}
