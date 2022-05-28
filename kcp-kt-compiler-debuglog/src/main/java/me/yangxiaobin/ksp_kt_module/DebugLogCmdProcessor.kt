package me.yangxiaobin.ksp_kt_module

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

@AutoService(CommandLineProcessor::class)
class DebugLogCmdProcessor : CommandLineProcessor {

    override val pluginId: String = "DebugLogSubPlugin"

    override val pluginOptions: Collection<CliOption> =
        listOf(
            CliOption(optionName = KEY_ENABLE,
                valueDescription = "<true|false>",
                description = "whether plugin is enabled."),

            CliOption(optionName = KEY_ANNOTATION,
                valueDescription = "<fqname>",
                description = "debug log annotation names",
                required = true,
                allowMultipleOccurrences = true),
        )


    override fun processOption(
        option: AbstractCliOption,
        value: String,
        configuration: CompilerConfiguration,
    ) {
        super.processOption(option, value, configuration)

        when (val optName = option.optionName) {
            KEY_ENABLE -> configuration.put(enableConfigurationKey, value.toBoolean())
            KEY_ANNOTATION -> configuration.appendList(annotationConfigurationKey, value)
            else -> error("Unexpected option name :$optName.")
        }

    }

    companion object{
        const val KEY_ENABLE = "enable"
        const val KEY_ANNOTATION = "debugLogAnnotation"

        val enableConfigurationKey = CompilerConfigurationKey<Boolean>(KEY_ENABLE)
        val annotationConfigurationKey = CompilerConfigurationKey<List<String>>(KEY_ANNOTATION)
    }


}
