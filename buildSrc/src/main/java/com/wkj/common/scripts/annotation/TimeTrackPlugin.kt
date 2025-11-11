package com.wkj.common.scripts.annotation

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import java.io.File

class TimeTrackPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("========================================")
        println("⏱️  TimeTrack Plugin Applied!")
        println("========================================")

        val config = project.extensions.create("timeTrack", TimeTrackConfig::class.java)

        project.afterEvaluate {
            if (!config.enabled.get()) {
                println("⚠️ TimeTrack is disabled!")
                return@afterEvaluate
            }

            printConfig(config)
            setupTaskHooks(project, config)
        }
    }

    private fun setupTaskHooks(project: Project, config: TimeTrackConfig) {
        println("\n🔧 Setting up Task Hooks...")

        var hooked = 0

        project.tasks.configureEach { task ->
            when {
                task.name.matches(Regex("compile.*Kotlin")) -> {
                    println("   🔗 Hooking: ${task.name}")
                    hooked++

                    task.doLast {
                        println("\n" + "=".repeat(80))
                        println("🚀 Processing after: ${task.name}")
                        println("=".repeat(80))
                        processClasses(project, config)
                    }
                }

                task.name.matches(Regex("compile.*Java")) -> {
                    println("   🔗 Hooking: ${task.name}")
                    hooked++

                    task.doLast {
                        println("\n" + "=".repeat(80))
                        println("🚀 Processing after: ${task.name}")
                        println("=".repeat(80))
                        processClasses(project, config)
                    }
                }
            }
        }

        if (hooked > 0) {
            println("✅ Hooked into $hooked compile tasks")
        } else {
            println("⚠️ No compile tasks found to hook")
        }
    }

    private fun processClasses(project: Project, config: TimeTrackConfig) {
        val buildDir = project.buildDir

        val classesDirs = listOf(
            File(buildDir, "tmp/kotlin-classes/debug"),
            File(buildDir, "tmp/kotlin-classes/release"),
            File(buildDir, "intermediates/javac/debug/classes"),
            File(buildDir, "intermediates/javac/release/classes")
        ).filter { it.exists() }

        if (classesDirs.isEmpty()) {
            println("⚠️ No compiled classes found")
            return
        }

        var totalClasses = 0
        var transformedClasses = 0

        classesDirs.forEach { classesDir ->
            println("\n📁 Processing: ${classesDir.absolutePath}")

            classesDir.walkTopDown()
                .filter { it.isFile && it.extension == "class" }
                .forEach { classFile ->
                    totalClasses++

                    val relativePath =
                        classFile.relativeTo(classesDir).path.replace(File.separator, "/")

                    if (shouldProcess(relativePath, config)) {
                        if (transformClassFile(classFile, config)) {
                            transformedClasses++
                        }
                    }
                }
        }

        println("\n" + "=".repeat(80))
        println("📊 Summary: $transformedClasses/$totalClasses classes transformed")
        println("=".repeat(80) + "\n")
    }

    private fun shouldProcess(path: String, config: TimeTrackConfig): Boolean {
        val normalizedPath = path.replace("\\", "/")

        if (config.excludes.get().any { matchPattern(normalizedPath, it) }) {
            return false
        }

        val includes = config.includes.get()
        if (includes.isNotEmpty()) {
            return includes.any { matchPattern(normalizedPath, it) }
        }

        if (!config.scanThirdParty.get()) {
            val thirdParty = listOf(
                "android/", "androidx/", "kotlin/", "kotlinx/",
                "java/", "javax/", "com/google/"
            )
            if (thirdParty.any { normalizedPath.startsWith(it) }) {
                return false
            }
        }

        return true
    }

    private fun matchPattern(path: String, pattern: String): Boolean {
        val regex = pattern
            .replace(".", "\\.")
            .replace("**", ".*")
            .replace("*", "[^/]*")
            .toRegex()
        return regex.matches(path)
    }

    private fun transformClassFile(classFile: File, config: TimeTrackConfig): Boolean {
        return try {
            val bytes = classFile.readBytes()
            val result = transformClass(bytes, config, classFile.name)

            if (result != null) {
                classFile.writeBytes(result)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            println("   ❌ Error: ${classFile.name} - ${e.message}")
            false
        }
    }

    private fun transformClass(
        bytes: ByteArray,
        config: TimeTrackConfig,
        fileName: String,
    ): ByteArray? {
        return try {
            val reader = ClassReader(bytes)
            val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)

            var hasAnnotation = false
            var className = ""

            val visitor = object : ClassVisitor(Opcodes.ASM9, writer) {
                override fun visit(
                    version: Int, access: Int, name: String, sig: String?,
                    superName: String?, interfaces: Array<out String>?,
                ) {
                    className = name
                    super.visit(version, access, name, sig, superName, interfaces)
                }

                override fun visitMethod(
                    access: Int, name: String, desc: String,
                    sig: String?, exceptions: Array<out String>?,
                ): MethodVisitor {
                    val mv = super.visitMethod(access, name, desc, sig, exceptions)

                    if (access and Opcodes.ACC_ABSTRACT != 0 ||
                        access and Opcodes.ACC_NATIVE != 0 ||
                        name == "<clinit>"
                    ) {
                        return mv
                    }

                    return TimeTrackMethodVisitor(mv, access, name, desc, className, config) {
                        hasAnnotation = true
                        println("   ✅ Found @TimeTrack: $className.$name")
                    }
                }
            }

            // ⭐⭐⭐ 关键修复：使用 EXPAND_FRAMES
            reader.accept(visitor, ClassReader.EXPAND_FRAMES)

            if (hasAnnotation) {
                println("   💾 Transformed: $fileName")
                writer.toByteArray()
            } else {
                null
            }

        } catch (e: Exception) {
            println("   ❌ Transform error: $fileName - ${e.message}")
            null
        }
    }

    private fun printConfig(config: TimeTrackConfig) {
        println("\n📝 TimeTrack Configuration:")
        println("   enabled: ${config.enabled.get()}")
        println("   tag: ${config.tag.get()}")
        println("   threshold: ${config.threshold.get()}ms")

        val includes = config.includes.get()
        if (includes.isNotEmpty()) {
            println("   includes:")
            includes.forEach { println("     • $it") }
        }

        println("   excludes: ${config.excludes.get().size} patterns")
        println("   scanThirdParty: ${config.scanThirdParty.get()}")
        println()
    }
}
