package me.yangxiaobin.android.codelab

import android.content.Context
import android.content.DialogInterface
import android.hardware.SensorManager
import me.yangxiaobin.android.codelab.debug.ShakeDetector
import me.yangxiaobin.android.kotlin.codelab.BuildConfig
import me.yangxiaobin.android.kotlin.codelab.base.AbsApplication
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.base.manager.ActivityRecorder
import me.yangxiaobin.android.kotlin.codelab.ext.appinfo.*
import me.yangxiaobin.android.kotlin.codelab.ext.device.*
import me.yangxiaobin.android.kotlin.codelab.ext.format2KMG
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.permission.PermissionManager
import me.yangxiaobin.logger.core.LogFacade
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.selector
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


class MyApp : AbsApplication() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "MyApp@@"

    override fun onCreate() {
        super.onCreate()
        init()
        //initFlutterEngine()
        initShakeDetector()
        testForDebugLog(2)
        testForBuildFields()
    }

    private fun initShakeDetector() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sd = ShakeDetector {
            logD("Detect shake")

            val items = listOf(
                "Jump to MainActivity",
                "Disable Quick Nav",
            )

            val ctx = ActivityRecorder.topActivity
            ctx.selector("Debug Settings", items) { _: DialogInterface, index: Int ->
                when (index) {
                    0 -> ctx.startActivity(intentFor<MainActivity>())
                    1 -> ctx.startActivity(intentFor<MainActivity>())
                    else -> Unit
                }
            }

        }
        sd.start(sensorManager)
    }

    private fun init() {
        logD(
            """
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
        """.trimIndent()
        )

    }

    /**
     * 为了能够动态修改 initial route，这里不用提前初始化 flutter engine
     * see https://flutter.cn/docs/development/ui/navigation
     */
//    private fun initFlutterEngine(){
//        val flutterEngine = FlutterEngine(this)
//        flutterEngine.navigationChannel.setInitialRoute("/abasa")
//        flutterEngine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
//        FlutterEngineCache.getInstance().put("global_flutter_engine",flutterEngine)
//    }


    @OptIn(ExperimentalTime::class)
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
    private fun testForBuildFields() {
        logD(
            """
            string field :${BuildConfig.STRING_BUILD_CONFIG_FIELD}
            boolean field :${BuildConfig.BOOLEAN_BUILD_CONFIG_FIELD}
            boolean field from build param :${BuildConfig.HAS_CONFIG}
        """.trimIndent()
        )
    }


}
