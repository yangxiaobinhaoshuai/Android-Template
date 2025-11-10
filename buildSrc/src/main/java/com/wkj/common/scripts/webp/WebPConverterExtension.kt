package com.wkj.common.scripts.webp

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

/**
 * WebP 转换插件配置
 */
abstract class WebPConverterExtension {

    /**
     * 是否启用插件
     */
    abstract val enabled: Property<Boolean>

    /**
     * WebP 压缩质量 (0-100)
     * 默认: 80
     */
    abstract val quality: Property<Int>

    /**
     * PNG 图片压缩质量 (0-100)
     * 默认: 100 (无损)
     */
    abstract val pngQuality: Property<Int>

    /**
     * 需要转换的图片格式
     * 默认: ["png", "jpg", "jpeg"]
     */
    abstract val formats: ListProperty<String>

    /**
     * 转换后是否删除原文件
     * 默认: false
     */
    abstract val deleteOriginal: Property<Boolean>

    /**
     * 是否跳过已存在的 WebP 文件
     * 默认: true
     */
    abstract val skipExisting: Property<Boolean>

    /**
     * 最小文件大小限制 (KB)
     * 小于此值的文件不转换
     * 默认: 0
     */
    abstract val minSizeKB: Property<Int>

    /**
     * 排除的目录名称
     * 默认: []
     */
    abstract val excludeDirs: ListProperty<String>

    /**
     * 排除的文件名称 (不含扩展名)
     * 默认: ["ic_launcher"]
     */
    abstract val excludeFiles: ListProperty<String>

    /**
     * 是否显示详细日志
     * 默认: true
     */
    abstract val verbose: Property<Boolean>

    /**
     * 执行时机
     * - "manual": 手动执行（默认）
     * - "preBuild": 在 preBuild 之前
     * - "beforeMerge": 在合并资源之前
     * - "release": 只在 Release 构建时
     */
    abstract val executionTiming: Property<String>

    init {
        enabled.convention(true)
        quality.convention(80)
        pngQuality.convention(100)
        formats.convention(listOf("png", "jpg", "jpeg"))
//        deleteOriginal.convention(false)
        deleteOriginal.convention(true)
        skipExisting.convention(true)
        minSizeKB.convention(0)
        excludeDirs.convention(emptyList())
        excludeFiles.convention(listOf("ic_launcher"))
        verbose.convention(true)
        executionTiming.convention("beforeMerge")  // 默认在合并资源前执行
    }
}
