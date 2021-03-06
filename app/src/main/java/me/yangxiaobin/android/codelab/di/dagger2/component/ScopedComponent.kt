package me.yangxiaobin.android.codelab.di.dagger2.component

import dagger.Component
import me.yangxiaobin.android.codelab.di.dagger2.Dagger2Fragment
import me.yangxiaobin.android.codelab.di.dagger2.di_module.MyScope

@MyScope
@Component
interface ScopedComponent {

    fun injectDagger2Fragment(fragment: Dagger2Fragment)
}
