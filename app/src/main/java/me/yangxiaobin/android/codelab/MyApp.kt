package me.yangxiaobin.android.codelab

import me.yangxiaobin.android.kotlin.codelab.base.AbsApplication
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.*
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class MyApp : AbsApplication() {


    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "MyApp@@"

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {

        logD("""
            rom : ${getRomName()}
            curApi : $currentApiLevel
            totalMem : ${getTotalMemory().format2KMG()}
            AvailMem : ${getAvailableMemory().format2KMG()}
            sdCardTotal : ${getSdCardTotalSpace().format2KMG()}
            sdCardAvail : ${getSdCardAvailableSpace().format2KMG()}
            cur net : ${this.getNetworkType()}
            AndroidId : ${this.getAndroidId}
            appName : ${this.getAppName}
            appVersionName : ${this.getAppVersionName}
            appVersionCode : ${this.getAppVersionCode}
            isDebuggable : ${this.isDebugBuildType}
            screen density : ${this.getDisplayDensity}
            targetApi : ${this.getTargetApi}
            processName  : $currentProcessName
            mac addr : ${this.getMacAddress()}
            DNS : ${this.getDNS()}
        """.trimIndent())

    }

}
