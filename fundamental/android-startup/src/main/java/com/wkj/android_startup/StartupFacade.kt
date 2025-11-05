package com.wkj.android_startup

import android.content.Context
import androidx.startup.Initializer
import com.wkj.android_startup.log.KLogger

private const val TAG = "StartupFacade"

class StartupFacade : Initializer<Unit> {

    override fun create(context: Context) {
        KLogger.d(TAG, " StartupFacade create ")

    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }


}

