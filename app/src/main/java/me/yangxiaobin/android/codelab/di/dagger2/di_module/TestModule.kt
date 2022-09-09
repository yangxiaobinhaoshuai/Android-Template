package me.yangxiaobin.android.codelab.di.dagger2.di_module

import dagger.Module
import dagger.Provides
import me.yangxiaobin.android.codelab.di.dagger2.pojo.StuffA

@Module
class TestModule {

    companion object {

        @MyScope
        @Provides
        @JvmStatic
        fun provideStuffA(): StuffA {
            return StuffA()
        }

    }
}
