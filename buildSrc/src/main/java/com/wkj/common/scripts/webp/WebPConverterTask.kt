package com.wkj.common.scripts.webp

import com.wkj.common.scripts.logger.error
import com.wkj.common.scripts.logger.lifecycle
import com.wkj.common.scripts.logger.myLogger
import com.wkj.common.scripts.logger.warn
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * WebP 转换任务
 */
abstract class WebPConverterTask : DefaultTask() {

    @get:InputDirectory
    abstract val resDirectory: DirectoryProperty

    @get:Input
    abstract val quality: Property<Int>

    @get:Input
    abstract val pngQuality: Property<Int>

    @get:Input
    abstract val formats: ListProperty<String>

    @get:Input
    abstract val deleteOriginal: Property<Boolean>

    @get:Input
    abstract val skipExisting: Property<Boolean>

    @get:Input
    abstract val minSizeKB: Property<Int>

    @get:Input
    abstract val excludeDirs: ListProperty<String>

    @get:Input
    abstract val excludeFiles: ListProperty<String>

    @get:Input
    abstract val verbose: Property<Boolean>

    private val logger = this.myLogger

    init {
        group = "webp"
        description = "Convert images to WebP format"

        // 禁用缓存，强制每次执行
        outputs.upToDateWhen { false }
    }

    private data class ConversionStats(
        var converted: Int = 0,
        var skipped: Int = 0,
        var failed: Int = 0,
        var totalOriginalSize: Long = 0,
        var totalWebpSize: Long = 0,
    )

    init {
        group = "resources"
        description = "Convert images to WebP format"
    }

    @TaskAction
    fun execute() {
        val cwebpPath = findCwebpTool()
            ?: throw GradleException(
                """
                |cwebp tool not found! Please install:
                |  • macOS:   brew install webp
                |  • Ubuntu:  sudo apt-get install webp
                |  • Windows: Download from https://developers.google.com/speed/webp/download
                |Or ensure Android SDK build-tools is installed.
                """.trimMargin()
            )

        val resDir = resDirectory.get().asFile
        if (!resDir.exists()) {
            logger.warn("Resource directory not found: ${resDir.absolutePath}")
            return
        }

        printHeader(cwebpPath)

        val stats = ConversionStats()
        val drawableDirs = findDrawableDirectories(resDir)

        if (drawableDirs.isEmpty()) {
            logger.warn("No drawable directories found in ${resDir.absolutePath}")
            return
        }

        drawableDirs.forEach { dir ->
            processDirectory(dir, cwebpPath, stats)
        }

        printSummary(stats)
    }

    private fun findDrawableDirectories(resDir: File): List<File> {
        val excludedDirs = excludeDirs.get()
        return resDir.listFiles()
            ?.filter {
                it.isDirectory &&
                        it.name.startsWith("drawable") &&
                        it.name !in excludedDirs
            }
            ?: emptyList()
    }

    private fun processDirectory(dir: File, cwebpPath: String, stats: ConversionStats) {
        if (verbose.get()) {
            logger.lifecycle("📁 Processing: ${dir.name}")
        }

        dir.listFiles()
            ?.filter { it.isFile }
            ?.forEach { file ->
                processFile(file, cwebpPath, stats)
            }
    }

    private fun processFile(file: File, cwebpPath: String, stats: ConversionStats) {
        val fileName = file.name
        val extension = file.extension.lowercase()

        // 检查文件格式
        if (extension !in formats.get()) {
            return
        }

        val nameWithoutExt = file.nameWithoutExtension

        // 检查排除列表
        if (excludeFiles.get().any { nameWithoutExt.contains(it, ignoreCase = true) }) {
            if (verbose.get()) {
                logger.lifecycle("  ⊘ Excluded: $fileName")
            }
            stats.skipped++
            return
        }

        // 检查文件大小
        val fileSizeKB = file.length() / 1024
        if (fileSizeKB < minSizeKB.get()) {
            if (verbose.get()) {
                logger.lifecycle("  ⊘ Too small (${fileSizeKB}KB): $fileName")
            }
            stats.skipped++
            return
        }

        val webpFile = File(file.parent, "$nameWithoutExt.webp")

        // 检查是否已存在
        if (webpFile.exists() && skipExisting.get()) {
            if (verbose.get()) {
                logger.lifecycle("  ⊘ Already exists: $fileName")
            }
            stats.skipped++
            return
        }

        // 执行转换
        convertToWebP(file, webpFile, extension, cwebpPath, stats)
    }

    private fun convertToWebP(
        sourceFile: File,
        targetFile: File,
        extension: String,
        cwebpPath: String,
        stats: ConversionStats,
    ) {
        try {
            val qualityValue = if (extension == "png") pngQuality.get() else quality.get()

            val command = buildCommand(cwebpPath, qualityValue, sourceFile, targetFile)
            val process = ProcessBuilder(command)
                .redirectErrorStream(true)
                .start()

            val exitCode = process.waitFor()

            if (exitCode == 0 && targetFile.exists()) {
                handleSuccessfulConversion(sourceFile, targetFile, stats)
            } else {
                handleFailedConversion(sourceFile, targetFile, process, stats)
            }
        } catch (e: Exception) {
            logger.error("  ✗ Error converting ${sourceFile.name}: ${e.message}")
            stats.failed++
        }
    }

    private fun buildCommand(
        cwebpPath: String,
        quality: Int,
        sourceFile: File,
        targetFile: File,
    ): List<String> = listOf(
        cwebpPath,
        "-q", quality.toString(),
        "-m", "6",  // 最高压缩方法
        "-mt",      // 多线程
        sourceFile.absolutePath,
        "-o", targetFile.absolutePath
    )

    private fun handleSuccessfulConversion(
        sourceFile: File,
        targetFile: File,
        stats: ConversionStats,
    ) {
        val originalSize = sourceFile.length()
        val webpSize = targetFile.length()
        val savedBytes = originalSize - webpSize
        val savingsPercent = (savedBytes.toDouble() / originalSize * 100)

        stats.converted++
        stats.totalOriginalSize += originalSize
        stats.totalWebpSize += webpSize

        if (verbose.get()) {
            val sign = if (savedBytes >= 0) "↓" else "↑"
            logger.lifecycle(
                "  ✅ ${sourceFile.name} → ${targetFile.name} " +
                        "(${formatSize(originalSize)} → ${formatSize(webpSize)}, " +
                        "$sign${String.format("%.1f", Math.abs(savingsPercent))}%)"
            )
        }

        if (deleteOriginal.get()) {
            sourceFile.delete()
        }
    }

    private fun handleFailedConversion(
        sourceFile: File,
        targetFile: File,
        process: Process,
        stats: ConversionStats,
    ) {
        val errorOutput = process.inputStream.bufferedReader().readText()
        logger.error("  ✗ Failed to convert ${sourceFile.name}")
        if (verbose.get() && errorOutput.isNotBlank()) {
            logger.error("     Error: $errorOutput")
        }
        targetFile.delete()
        stats.failed++
    }

    private fun findCwebpTool(): String? {
        // 1. 从 Android SDK 查找
        findInAndroidSDK()?.let { return it }

        // 2. 从 PATH 查找
        findInPath()?.let { return it }

        // 3. 从常见路径查找
        findInCommonPaths()?.let { return it }

        return null
    }

    private fun findInAndroidSDK(): String? {
        val androidHome = System.getenv("ANDROID_HOME")
            ?: System.getenv("ANDROID_SDK_ROOT")
            ?: return null

        val buildToolsDir = File(androidHome, "build-tools")
        if (!buildToolsDir.exists()) return null

        val versions = buildToolsDir.listFiles()
            ?.filter { it.isDirectory }
            ?.sortedByDescending { it.name }
            ?: return null

        val isWindows = System.getProperty("os.name").lowercase().contains("windows")
        val toolName = if (isWindows) "cwebp.exe" else "cwebp"

        return versions
            .asSequence()
            .map { File(it, toolName) }
            .firstOrNull { it.exists() && it.canExecute() }
            ?.absolutePath
    }

    private fun findInPath(): String? {
        return try {
            val isWindows = System.getProperty("os.name").lowercase().contains("windows")
            val command = if (isWindows) "where cwebp" else "which cwebp"

            val process = Runtime.getRuntime().exec(command)
            process.waitFor()

            if (process.exitValue() == 0) {
                process.inputStream.bufferedReader().readLine()?.trim()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun findInCommonPaths(): String? {
        val isWindows = System.getProperty("os.name").lowercase().contains("windows")

        val paths = if (isWindows) {
            listOf(
                "C:\\webp\\bin\\cwebp.exe",
                "C:\\Program Files\\WebP\\cwebp.exe"
            )
        } else {
            listOf(
                "/usr/local/bin/cwebp",
                "/usr/bin/cwebp",
                "/opt/homebrew/bin/cwebp"
            )
        }

        return paths.firstOrNull { File(it).exists() }
    }

    private fun formatSize(bytes: Long): String = when {
        bytes < 1024 -> "${bytes}B"
        bytes < 1024 * 1024 -> String.format("%.2fKB", bytes / 1024.0)
        else -> String.format("%.2fMB", bytes / 1024.0 / 1024.0)
    }

    private fun printHeader(cwebpPath: String) {
        logger.lifecycle("")
        logger.lifecycle("╔═══════════════════════════════════════════════════════════╗")
        logger.lifecycle("║           WebP Converter - Image Optimization            ║")
        logger.lifecycle("╠═══════════════════════════════════════════════════════════╣")
        logger.lifecycle("║ Tool:    ${cwebpPath.takeLast(50).padEnd(50)} ║")
        logger.lifecycle("║ Quality: ${quality.get()} (PNG: ${pngQuality.get()})".padEnd(60) + "║")
        logger.lifecycle("║ Formats: ${formats.get().joinToString()}".padEnd(60) + "║")
        logger.lifecycle("╚═══════════════════════════════════════════════════════════╝")
        logger.lifecycle("")
    }

    private fun printSummary(stats: ConversionStats) {
        val totalSaved = stats.totalOriginalSize - stats.totalWebpSize
        val savingsPercent = if (stats.totalOriginalSize > 0) {
            (totalSaved.toDouble() / stats.totalOriginalSize * 100)
        } else 0.0

        logger.lifecycle("")
        logger.lifecycle("╔═══════════════════════════════════════════════════════════╗")
        logger.lifecycle("║                    Conversion Summary                     ║")
        logger.lifecycle("╠═══════════════════════════════════════════════════════════╣")
        logger.lifecycle("║ ✅ Converted:     ${stats.converted.toString().padEnd(44)}║")
        logger.lifecycle("║ ⊘  Skipped:       ${stats.skipped.toString().padEnd(44)}║")
        logger.lifecycle("║ ✗  Failed:        ${stats.failed.toString().padEnd(44)}║")

        if (stats.converted > 0) {
            logger.lifecycle("╠═══════════════════════════════════════════════════════════╣")
            logger.lifecycle("║ Original Size:  ${formatSize(stats.totalOriginalSize).padEnd(44)}║")
            logger.lifecycle("║ WebP Size:      ${formatSize(stats.totalWebpSize).padEnd(44)}║")
            logger.lifecycle("║ Saved:          ${formatSize(totalSaved).padEnd(44)}║")
            logger.lifecycle(
                "║ Savings:        ${
                    String.format("%.2f%%", savingsPercent).padEnd(44)
                }║"
            )
        }

        logger.lifecycle("╚═══════════════════════════════════════════════════════════╝")
        logger.lifecycle("")
    }
}
