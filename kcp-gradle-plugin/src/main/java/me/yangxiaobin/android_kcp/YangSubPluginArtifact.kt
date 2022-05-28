package me.yangxiaobin.android_kcp

import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact

class YangSubPluginArtifact(
    groupId: String = GROUP_NAME,
    artifactId: String,
    version: String = "0.0.1",
) : SubpluginArtifact(groupId, artifactId, version)
