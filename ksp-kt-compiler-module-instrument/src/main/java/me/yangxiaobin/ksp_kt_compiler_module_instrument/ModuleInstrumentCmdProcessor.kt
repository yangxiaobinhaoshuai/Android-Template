package me.yangxiaobin.ksp_kt_compiler_module_instrument

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor

@AutoService(CommandLineProcessor::class)
class ModuleInstrumentCmdProcessor : CommandLineProcessor {

    override val pluginId: String = "moduleInstrument"

    override val pluginOptions: Collection<AbstractCliOption> = emptyList()

}
