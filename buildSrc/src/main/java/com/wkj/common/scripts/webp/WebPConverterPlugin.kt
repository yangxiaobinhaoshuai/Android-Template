package com.wkj.common.scripts.webp

import com.wkj.common.scripts.logger.myLogger
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File


/**
 * WebP 转换 Gradle 插件
 */
class WebPConverterPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        myLogger.e("this ${project.name} applied plugin ---->")

        // 创建扩展配置
        val extension = project.extensions.create(
            "webpConverter",
            WebPConverterExtension::class.java
        )


        project.plugins.withId("com.android.application") {
            project.afterEvaluate {
                val convertTask = project.tasks.register(
                    "convertToWebP",
                    WebPConverterTask::class.java
                ) { t ->
                    configureTask(project, extension, t)
                }

                // ✅ 根据配置决定何时执行
                if (extension.enabled.get()) {
                    when (extension.executionTiming.get()) {
                        "manual" -> {
                            // 不设置依赖，需要手动执行
                            println("ℹ️  convertToWebP: manual execution mode")
                        }

                        "preBuild" -> {
                            project.tasks.named("preBuild").configure {
                                it.dependsOn(convertTask)
                            }
                            println("✅ convertToWebP will run before preBuild")
                        }

                        "beforeMerge" -> {
                            project.tasks.matching { task ->
                                task.name.matches(Regex("merge.*Resources"))
                            }.configureEach { mergeTask ->
                                mergeTask.dependsOn(convertTask)
                            }
                            println("✅ convertToWebP will run before merge resources")
                        }

                        "release" -> {
                            project.tasks.matching { task ->
                                task.name == "mergeReleaseResources"
                            }.configureEach { mergeTask ->
                                mergeTask.dependsOn(convertTask)
                            }
                            println("✅ convertToWebP will run before release build")
                        }

                        else -> {
                            println("⚠️  Unknown execution timing: ${extension.executionTiming.get()}")
                        }
                    }
                }
            }
        }

    }

    private fun configureTask(
        project: Project,
        extension: WebPConverterExtension,
        task: WebPConverterTask,
    ) {
        val resDir = File(project.projectDir, "src/main/res")

        // 配置任务
        with(task) {
            resDirectory.set(resDir)
            quality.set(extension.quality)
            pngQuality.set(extension.pngQuality)
            formats.set(extension.formats)
            deleteOriginal.set(extension.deleteOriginal)
            skipExisting.set(extension.skipExisting)
            minSizeKB.set(extension.minSizeKB)
            excludeDirs.set(extension.excludeDirs)
            excludeFiles.set(extension.excludeFiles)
            verbose.set(extension.verbose)

            onlyIf { extension.enabled.get() }
        }
    }
}
