package com.wkj.common.scripts.logger

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle


class LogPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        this.myLogger.e("this ${target.name} applied plugin ---->")

        target.gradle.addBuildListener(object : BuildListener {
            override fun settingsEvaluated(settings: Settings) {
                myLogger.e("this ${target.name}  settingsEvaluated---->")
            }

            override fun projectsLoaded(gradle: Gradle) {
                myLogger.e("this ${target.name} projectsLoaded ---->")
            }

            override fun projectsEvaluated(gradle: Gradle) {
                myLogger.e("this ${target.name} projectsEvaluated ---->")
            }

            override fun buildFinished(result: BuildResult) {
                myLogger.e("this ${target.name} buildFinished ---->")
            }
        })
    }
}
