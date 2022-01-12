package me.yangxiaobin.android.codelab.di.dagger2

import dagger.Module
import dagger.Provides

@Module
class DaggerModule {


    fun provideStuffA(): StuffA {
        return StuffA()
    }

    @Provides
    fun provideStuffB():StuffB{
        return StuffB()
    }
}
