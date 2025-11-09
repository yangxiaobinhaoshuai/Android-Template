package com.wkj.common.scripts

import org.gradle.api.Plugin
import org.gradle.api.Project


class LogPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        // TODO
        logger.e("this ${target.name} applied plugin ---->")
    }
}
