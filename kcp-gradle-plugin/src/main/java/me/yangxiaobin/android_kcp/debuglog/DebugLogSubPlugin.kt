package me.yangxiaobin.android_kcp.debuglog

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
class DebugLogSubPlugin : KotlinCompilerPluginSupportPlugin {

    override fun apply(target: Project) {
        super.apply(target)

        PLog.d("DebugLogSubPlugin apply to ${target.name}.")
        target.extensions.create("debugLog", DebugLogExtension::class.java)
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val p = kotlinCompilation.target.project
        val ext: DebugLogExtension = p.extensions.findByType(DebugLogExtension::class.java) ?: DEFAULT_EXT

        if (ext.enable && ext.annotationList.isEmpty()) error("enable debugLog but your annotation list is empty.")

        return p.provider {
            val list: List<SubpluginOption> =
                ext.annotationList.map {
                    YangSubPluginOption(optKey = SUB_PLUGIN_OPTION_KEY_ANNOTATION, optVal = it)
                } + YangSubPluginOption(optKey = SUB_PLUGIN_OPTION_KEY_ENABLE, optVal = ext.enable.toString())

            //PLog.d("SubPlugin provide sub options: $list.")

            list
        }
    }

    override fun getCompilerPluginId(): String = DEBUG_LOG_PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact = YangSubPluginArtifact(artifactId = "kcp-kt-debuglog")

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true


    private companion object {
        const val DEBUG_LOG_PLUGIN_ID = "DebugLogSubPlugin"

        const val SUB_PLUGIN_OPTION_KEY_ENABLE = "enable"
        const val SUB_PLUGIN_OPTION_KEY_ANNOTATION = "debugLogAnnotation"

        val DEFAULT_EXT = DebugLogExtension()
    }
}

open class DebugLogExtension {
    var enable: Boolean = false
    var annotationList: List<String> = emptyList()
}
