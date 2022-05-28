package me.yangxiaobin.android_kcp.module_service

import me.yangxiaobin.android_kcp.PLog
import org.gradle.api.Plugin
import org.gradle.api.Project


class ModuleServicePlugin : Plugin<Project> {

    override fun apply(p: Project) {
        PLog.d("ModuleServicePlugin applied to ${p.name}.")
    }


}
