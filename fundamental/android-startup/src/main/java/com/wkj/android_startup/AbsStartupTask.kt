package com.wkj.android_startup

import androidx.startup.Initializer

abstract class AbsStartupTask : StartupAware {


    override fun create(context: android.content.Context) {
        // No implementation
    }

    override fun dependencies(): MutableList<Class<out StartupAware>> {
        return super.dependencies()
    }
}
