package me.yangxiaobin.ksp_kt_compiler_module_instrument

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration


/**
 *  因为 kcp 仅适用于单独 module 内的 kt 文件，其局限性无法实现 module api provide 需求
 *
 *  顾废弃

 *  TODO
 */
@AutoService(ComponentRegistrar::class)
class ModuleInstrumentComponentRegistrar : ComponentRegistrar {

    override fun registerProjectComponents(
        project: MockProject,
        configuration: CompilerConfiguration,
    ) {

        val annotations: List<String> = configuration.get(ModuleInstrumentCmdProcessor.annotationConfigurationKey, emptyList())
    }
}
