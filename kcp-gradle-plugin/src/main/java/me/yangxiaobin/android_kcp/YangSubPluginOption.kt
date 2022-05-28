package me.yangxiaobin.android_kcp

import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

data class YangSubPluginOption(val optKey: String, val optVal: String) : SubpluginOption(optKey, lazyOf(optVal))
