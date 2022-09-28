package me.yangxiaobin.android.di_lib.component

import dagger.Component
import me.yangxiaobin.android.di_lib.Dagger2Fragment
import me.yangxiaobin.android.di_lib.di_module.MyScope
import me.yangxiaobin.android.di_lib.pojo.StuffB

@MyScope
@Component
interface ScopedComponent {

    fun getMyScopedB(): StuffB

    fun injectDagger2Fragment(fragment: Dagger2Fragment)
}
