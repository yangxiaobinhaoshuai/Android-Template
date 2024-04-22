package me.yangxiaobin.android.di_lib.di_module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.yangxiaobin.android.di_lib.pojo.StuffA

@InstallIn(SingletonComponent::class)
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
