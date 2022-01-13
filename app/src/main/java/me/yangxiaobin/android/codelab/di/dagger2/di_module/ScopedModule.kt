package me.yangxiaobin.android.codelab.di.dagger2.di_module

import dagger.Module
import dagger.Provides
import me.yangxiaobin.android.codelab.di.dagger2.pojo.StuffC
import javax.inject.Scope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
annotation class MyScope


@Module
class ScopedModule {

}
