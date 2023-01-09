package me.yangxiaobin.android.di_lib

import android.os.Bundle
import android.view.View
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import me.yangxiaobin.android.di_lib.pojo.StuffB
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.uicontroller.showFragmentToast
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment
import me.yangxiaobin.logger.core.LogFacade
import javax.inject.Inject

class Dagger2Fragment : AbsComposableFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "Dagger2Demo"

//    @Inject
//    lateinit var stuffA1: StuffA
//
//    @Inject
//    lateinit var stuffA2: StuffA

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
        }) {

            Text(text = "Dagger2 Example.")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logDI(" onViewCreated.")

        // DaggerUnScopedComponent.create().injectDagger2Fragment(this)
        //DaggerScopedComponent.create().injectDagger2Fragment(this)

//        stuffA1.work()
//        stuffA2.work()

        stuffB1.work()
        stuffB2.work()
    }
}
