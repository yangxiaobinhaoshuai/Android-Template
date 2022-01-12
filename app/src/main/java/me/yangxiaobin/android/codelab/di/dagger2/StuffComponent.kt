package me.yangxiaobin.android.codelab.di.dagger2

import dagger.Component

@Component
interface StuffComponent {

    fun injectDagger2Fragment(fragment: Dagger2Fragment)
}
