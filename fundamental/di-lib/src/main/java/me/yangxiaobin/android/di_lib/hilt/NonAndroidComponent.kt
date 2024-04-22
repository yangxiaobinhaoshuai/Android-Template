package me.yangxiaobin.android.di_lib.hilt

import android.content.Context
import android.util.Log
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import me.yangxiaobin.android.di_lib.pojo.StuffA
import javax.inject.Inject


@EntryPoint
@InstallIn(SingletonComponent::class)
interface NonAndroidInterface {
    fun inject(non: NonAndroidComponent)

    fun getSuffA(): StuffA
}

class NonAndroidComponent {

    @Inject
    lateinit var stuffA: StuffA

    init {
        //EntryPointAccessors.fromApplication()
        //EntryPoints.get(applicationContent, NonAndroidInterface::class.java)
    }
    fun test(context:Context) {
        EntryPointAccessors.fromApplication(context.applicationContext,NonAndroidInterface::class.java).inject(this)
        stuffA.work()
        Log.d("NonAndroidComponent", "stuffA: $stuffA.")
    }

    companion object StaticObj {

        fun staticTest(context: Context) {

           val stuffb =  EntryPointAccessors.fromApplication(
                context.applicationContext,
                NonAndroidInterface::class.java
            ).getSuffA()

            stuffb.work()
            Log.d("NonAndroidComponent", "stuffB: $stuffb.")
        }
    }
}
