package com.wkj.common.scripts.annotation

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import java.io.Serializable

abstract class TimeTrackConfig : Serializable {
    @get:Input
    abstract val enabled: Property<Boolean>

    @get:Input
    abstract val tag: Property<String>

    @get:Input
    abstract val threshold: Property<Long>

    @get:Input
    abstract val includes: ListProperty<String>

    @get:Input
    abstract val excludes: ListProperty<String>

    @get:Input
    abstract val scanThirdParty: Property<Boolean>

    init {
        enabled.convention(true)
        tag.convention("TimeTrack")
        threshold.convention(0L)
        includes.convention(emptyList())
        excludes.convention(
            listOf(
                "android/**",
                "androidx/**",
                "kotlin/**",
                "kotlinx/**",
                "java/**",
                "javax/**",
                "com/google/**",
                "**/R.class",
                "**/R\$*.class",
                "**/BuildConfig.class"
            )
        )
        scanThirdParty.convention(false)
    }
}
