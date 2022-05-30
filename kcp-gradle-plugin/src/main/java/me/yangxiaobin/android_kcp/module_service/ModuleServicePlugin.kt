package me.yangxiaobin.android_kcp.module_service

import com.google.auto.service.AutoService
import me.yangxiaobin.android_kcp.PLog
import me.yangxiaobin.android_kcp.YangSubPluginArtifact
import me.yangxiaobin.android_kcp.YangSubPluginOption
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption


@AutoService(KotlinCompilerPluginSupportPlugin::class)
class ModuleServicePlugin : KotlinCompilerPluginSupportPlugin {

    override fun apply(target: Project) {
        PLog.d("ModuleServicePlugin applied to ${target.name}.")

        target.extensions.create("moduleService", ModuleServiceExt::class.java)
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val p: Project = kotlinCompilation.target.project

        val ext: ModuleServiceExt? = p.extensions.findByType(ModuleServiceExt::class.java)

        val annotationList: List<String> = ext?.annotations ?: emptyList()


        return kotlinCompilation.target.project.provider {
            annotationList.map { annotation: String ->
                YangSubPluginOption(optKey = KEY_ANNOTATION_SUB_PLUGIN_OPTION, optVal = annotation)
            }
        }
    }

    override fun getCompilerPluginId(): String = MODULE_INSTRUMENT_PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact = YangSubPluginArtifact(artifactId = "kcp-kt-module-instrument")

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    private companion object {
        const val MODULE_INSTRUMENT_PLUGIN_ID = "moduleInstrument"
        const val KEY_ANNOTATION_SUB_PLUGIN_OPTION = "annotation"
    }
}

open class ModuleServiceExt{
    var annotations: List<String> = emptyList()
}
