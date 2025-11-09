package com.wkj.common.scripts

import com.wkj.common.scripts.logger.GradleLoggerAware
import com.wkj.common.scripts.logger.TaggedLogger
import com.wkj.common.scripts.logger.withTag
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logging


val Plugin<Project>.logger: TaggedLogger
    get() {
        val simpleName = this.javaClass.simpleName
        val gradleLogger = Logging.getLogger(simpleName)
        val loggerAware = GradleLoggerAware(gradleLogger, simpleName)
        return loggerAware.withTag(simpleName)
    }
