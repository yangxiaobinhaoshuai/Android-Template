package me.yangxiaobin.android_kcp

import org.gradle.api.Plugin
import org.gradle.api.Project


class ModuleServicePlugin : Plugin<Project> {

    override fun apply(p: Project) {
        Logger.d("ModuleServicePlugin applied to ${p.name} ==>")
    }


}
