package com.wkj.android_startup

import androidx.startup.Initializer


interface StartupAware : Initializer<Unit> {

    fun name(): String

    fun inMainThread(): Boolean = true

    fun afterOnCreate(): Boolean = true

    override fun dependencies(): MutableList<Class<out StartupAware>> = mutableListOf()
}
