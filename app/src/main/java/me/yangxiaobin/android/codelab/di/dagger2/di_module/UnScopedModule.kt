package me.yangxiaobin.android.codelab.di.dagger2.di_module

import dagger.Module
import dagger.Provides
import me.yangxiaobin.android.codelab.di.dagger2.pojo.StuffA
import javax.inject.Singleton

@Module
class UnScopedModule {


    @Module
    companion object {

        @Singleton
        @Provides
        @JvmStatic
        fun provideStuffA(): StuffA {
            return StuffA()
        }

    }


//    @Provides
//    fun provideStuffB(): StuffB {
//        return StuffB()
//    }
}
