package me.yangxiaobin.android_kcp.module_service

import com.google.auto.service.AutoService
import me.yangxiaobin.android_kcp.PLog
import me.yangxiaobin.android_kcp.YangSubPluginArtifact
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption


@AutoService(KotlinCompilerPluginSupportPlugin::class)
class ModuleServicePlugin : KotlinCompilerPluginSupportPlugin {

    override fun apply(p: Project) {
        PLog.d("ModuleServicePlugin applied to ${p.name}.")

    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        return kotlinCompilation.target.project.provider { emptyList() }
    }

    override fun getCompilerPluginId(): String = MODULE_INSTRUMENT_PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact =
        YangSubPluginArtifact(artifactId = "kcp-kt-module-instrument")

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    private companion object {
        const val MODULE_INSTRUMENT_PLUGIN_ID = "moduleInstrument"
    }
}
