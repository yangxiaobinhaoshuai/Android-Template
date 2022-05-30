package me.yangxiaobin.ksp_kt_compiler_module_instrument

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

@AutoService(CommandLineProcessor::class)
class ModuleInstrumentCmdProcessor : CommandLineProcessor {

    override val pluginId: String = PLUGIN_ID

    override val pluginOptions: Collection<AbstractCliOption> = listOf(
        CliOption(
            optionName = KEY_ANNOTATION,
            valueDescription = "<fqname>",
            description = "module service annotation name",
            required = true,
            allowMultipleOccurrences = true
        ),
    )

    override fun processOption(
        option: AbstractCliOption,
        value: String,
        configuration: CompilerConfiguration,
    ) {
        super.processOption(option, value, configuration)

        when (val optName = option.optionName) {
            KEY_ANNOTATION -> configuration.appendList(annotationConfigurationKey, value)
            else -> error("Unexpected option name :$optName.")
        }

    }

    companion object {
        private const val PLUGIN_ID = "moduleInstrument"
        private const val KEY_ANNOTATION = "annotation"

        val annotationConfigurationKey = CompilerConfigurationKey<List<String>>(KEY_ANNOTATION)
    }

}
