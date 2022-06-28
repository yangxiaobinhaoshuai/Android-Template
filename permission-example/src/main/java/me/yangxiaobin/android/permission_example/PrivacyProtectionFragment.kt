package me.yangxiaobin.android.permission_example

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.*
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.permission.PermissionManager
import me.yangxiaobin.common_ui.ButtonsFragment
import me.yangxiaobin.logger.core.LogFacade


/**
 * 用于测试小米手机，手机管家-> 隐私保护 -> 查看全部应用行为 -> 特定应用行为记录
 *
 * 获取位置信息
 * 在后台获取位置信息
 * 录音
 * 访问了社交应用文件
 * 访问了相册
 * 读写设备上的照片及文件
 *
 *
 * 结论：
 * 对于小米手机 媒体音量控制、读取应用列表、读取剪切板、写入剪切板 都是默认提供的
 *
 */
class PrivacyProtectionFragment : ButtonsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "PrivacyProtection"


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        applyPermissions()
    }


    override fun onClick(index: Int) {
        super.onClick(index)

        when (index) {
            0 -> getLocationInfo()
            1 -> getDeviceInfo()
            2-> accessSysAlbum()
            else -> Unit
        }
    }

    /**
     * 访问存储文件或者相册
     */
    private fun accessSysAlbum(){

        PermissionManager.createReq(this).request(Manifest.permission.READ_EXTERNAL_STORAGE){
            onGranted {
                showFragmentToast("READ_EXTERNAL_STORAGE success")
            }

        }.request(Manifest.permission.WRITE_EXTERNAL_STORAGE){
            onGranted {
                showFragmentToast("WRITE_EXTERNAL_STORAGE success")
            }
        }.request( android.Manifest.permission.READ_PHONE_STATE){
            onGranted {
                showFragmentToast("READ_PHONE_STATE success")
            }
        }

    }

    private fun getLocationInfo(){

        PermissionManager.without(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION){return}

        // 获取的是位置服务
        val serviceString: String = Context.LOCATION_SERVICE

        // 调用getSystemService()方法来获取LocationManager对象
        val locationManager = requireContext().getSystemService(serviceString) as LocationManager

        // 指定LocationManager的定位方法
        val provider = LocationManager.GPS_PROVIDER

        @SuppressLint("MissingPermission")
        // 调用getLastKnownLocation()方法获取当前的位置信息
        val location: Location? = locationManager.getLastKnownLocation(provider)

        //获取纬度
        val lat = location?.latitude

        //获取经度
        val lng = location?.longitude

        logD("Thread :${Thread.currentThread().name} lat :$lat, lng :$lng.")
    }


    private fun applyPermissions() {

        applyLocationInfo()

    }

    /**
     * 请求位置 Android Doc ：https://developer.android.com/training/location/permissions?hl=zh-cn
     * 类别前台和后台
     */
    private fun applyLocationInfo() {
        applyLocationInfoForeground()
        applyLocationInfoBackground()
    }

    /**
     * 1. Activity 前台可见
     * 2. 前台服务
     */
    private fun applyLocationInfoForeground() {
        PermissionManager.createReq(this).request(Manifest.permission.ACCESS_COARSE_LOCATION) {
            onGranted {
                showFragmentToast("Location permission granted!")
            }

            shouldShowRationale {
                showFragmentToast("Location permission denied!")
            }

            onNeverAskAgain {
                logD("Location permission never ask again.")
            }
        }
    }

    /**
     * 除去前台类型，其他任何类型获取位置行为都称为 后台获取位置信息
     *
     * 在 Android 10（API 级别 29）及更高版本中，您必须在应用的清单中声明 ACCESS_BACKGROUND_LOCATION 权限
     *
     * 较低版本的 Android 系统中，当应用获得前台位置信息访问权限时，也会自动获得后台位置信息访问权限。
     */
    private fun applyLocationInfoBackground() {

        lifecycleScope.launch(Dispatchers.IO) {

            while (true){
                delay(200)
                getLocationInfo()
            }
        }
    }

    /**
     * 获取唯一标识 Doc ：https://developer.android.com/training/articles/user-data-ids?hl=zh-cn
     */
    private fun getDeviceInfo(){
        logD("""
            getDeviceInfo:
            rom : ${getRomName()}
            curApi : $currentApiLevel
            totalMem : ${requireContext().getTotalMemory().format2KMG()}
            AvailMem : ${requireContext().getAvailableMemory().format2KMG()}
            sdCardTotal : ${getSdCardTotalSpace().format2KMG()}
            sdCardAvail : ${getSdCardAvailableSpace().format2KMG()}
            AndroidId : ${requireContext().getAndroidId}
            appName : ${requireContext().getAppName}
            appVersionName : ${requireContext().getAppVersionName}
            appVersionCode : ${requireContext().getAppVersionCode}
            isDebuggable : ${requireContext().isDebugBuildType}
            screen density : ${requireContext().getDisplayDensity}
            targetApi : ${requireContext().getTargetApi}
            processName  : $currentProcessName
            DNS : ${requireContext().getDNS()}
        """.trimIndent())
    }

}
