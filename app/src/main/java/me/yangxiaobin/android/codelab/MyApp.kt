package me.yangxiaobin.android.codelab

import me.yangxiaobin.android.kotlin.codelab.BuildConfig
import me.yangxiaobin.android.kotlin.codelab.base.AbsApplication
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.*
import me.yangxiaobin.android.kotlin.codelab.ext.appinfo.*
import me.yangxiaobin.android.kotlin.codelab.ext.device.*
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.permission.PermissionManager
import me.yangxiaobin.logger.core.LogFacade
import me.yangxiaobin.module_service_provider_annotation.DebugLog
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
class MyApp : AbsApplication() {


    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "MyApp@@"

    override fun onCreate() {
        super.onCreate()
        init()
        testForDebugLog(2)
        PermissionManager.registerPermissionAccessListener {
            logD("permission accessed :$it.")
        }
        testForBuildFields()
    }

    @DebugLog
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


    @DebugLog
    private fun testForDebugLog(value: Int): Boolean {
        measureTimedValue {
            if (value == 1) return@measureTimedValue 100L
            else return@measureTimedValue 200L
        }
        return false
    }


    /**
     * -PhasConfig=true
     * -PhasConfig=false
     * 未赋值为 null
     */
    private fun testForBuildFields(){
        logD("""
            string field :${BuildConfig.STRING_BUILD_CONFIG_FIELD}
            boolean field :${BuildConfig.BOOLEAN_BUILD_CONFIG_FIELD}
            boolean field from build param :${BuildConfig.HAS_CONFIG}
        """.trimIndent())
    }


}
