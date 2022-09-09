package me.yangxiaobin.android.codelab.di.dagger2

import android.os.Bundle
import android.view.View
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import me.yangxiaobin.android.codelab.di.dagger2.component.DaggerScopedComponent
import me.yangxiaobin.android.codelab.di.dagger2.pojo.StuffA
import me.yangxiaobin.android.codelab.di.dagger2.pojo.StuffB
import me.yangxiaobin.android.codelab.npe.NPECreator
import me.yangxiaobin.android.kotlin.codelab.ext.showFragmentToast
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment
import javax.inject.Inject

@Deprecated("Migrate to di-lib module")
class Dagger2Fragment : AbsComposableFragment() {

    @Inject
    lateinit var stuffA: StuffA

    @Inject
    lateinit var stuffB1: StuffB

    @Inject
    lateinit var stuffB2: StuffB
//
//    @Inject
//    lateinit var stuffC: StuffC


    init {
        logDI("Dagger2Fragment init.")
    }


    override val composableContent: @Composable () -> Unit = {

        Button(onClick = {
            showFragmentToast("Click the bt.")
            NPECreator().createNPE()
        }) {

            Text(text = "Dagger2 Example.")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logDI(" onViewCreated.")

       // DaggerUnScopedComponent.create().injectDagger2Fragment(this)
        DaggerScopedComponent.create().injectDagger2Fragment(this)

        stuffA.work()
        stuffB1.work()
        stuffB2.work()
//        stuffC.work()
    }
}
