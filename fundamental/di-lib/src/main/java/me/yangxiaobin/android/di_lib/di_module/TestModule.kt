package me.yangxiaobin.android.di_lib.di_module

import dagger.Module
import dagger.Provides
import me.yangxiaobin.android.di_lib.pojo.StuffA

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
