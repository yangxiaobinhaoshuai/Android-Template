package me.yangxiaobin.ksp_kt_module

import com.google.auto.service.AutoService
import me.yangxiaobin.ksp_kt_module.ir.DebugLogIrOperator
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

@AutoService(ComponentRegistrar::class)
class DebugLogComponentRegistrar : ComponentRegistrar {

    override fun registerProjectComponents(
        project: MockProject,
        configuration: CompilerConfiguration,
    ) {
        val enable: Boolean = configuration.get(DebugLogCmdProcessor.enableConfigurationKey, false)
        val annotations: List<String> =
            configuration.get(DebugLogCmdProcessor.annotationConfigurationKey, emptyList())

        if (!enable) return

        // instead of logging.
        //error("param : enable: $enable, annotation :$annotations.")
        IrGenerationExtension.registerExtension(
            project,
            DebugLogIRExt(annotations)
        )


    }
}


class DebugLogIRExt(private val annotations: List<String>) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.transformChildrenVoid(DebugLogIrOperator(annotations, pluginContext))
    }

}

