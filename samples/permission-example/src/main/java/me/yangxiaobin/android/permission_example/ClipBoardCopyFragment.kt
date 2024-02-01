package me.yangxiaobin.android.permission_example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.children
import me.yangxiaobin.android.android_icons.IconLib
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.permission.AndroidPermission
import me.yangxiaobin.android.permission.PermissionManager
import me.yangxiaobin.android.permission.PermissionReqOption
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.common_ui.EmptyFragment
import me.yangxiaobin.logger.core.LogFacade

class ClipBoardCopyFragment : EmptyFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "ClipBoardCopyFragment"


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
        PermissionManager.configGlobally(PermissionReqOption(enableLog = true))
    }

    override fun getBackgroundColor(): Int = HexColors.GRAY_500.colorInt

    override fun onButtonClick(index: Int, v: View) {
        super.onButtonClick(index, v)
        // <uses-permission android:name="android.permission.READ_CLIPBOARD"/>
        val clipboardManager: ClipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData: ClipData? = clipboardManager.primaryClip

        when (index) {
            0 -> {

//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
//                intent.data = uri
//
//                if (intent.resolveActivity(requireContext().packageManager) != null) {
//                    requireContext().startActivity(intent)
//                }


//                val settingIntent = Intent(Settings.ACTION_SETTINGS)
//                if (settingIntent.resolveActivity(requireContext().packageManager) != null) {
//                    requireContext().startActivity(settingIntent)
//                }

                // <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
                //在 6.0 以前的系统版本，悬浮窗权限是默认开启的，直接使用即可。
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (!Settings.canDrawOverlays(context)) {
//                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:${requireContext().packageName}"))
//                        startActivity(intent)
//                        return;
//                    }
//                }

                PermissionManager
                    .registerPermissionInterceptor(AndroidPermission.SYSTEM_ALERT_WINDOW) { i ->
                        i.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                        i.data = Uri.parse("package:${requireContext().packageName}")
                        true
                    }
                    .createReq(this).request(AndroidPermission.SYSTEM_ALERT_WINDOW){
                        this.onGranted {
                            logD("onGranted : ${it.contentToString()}")
                        }
                        this.onNeverAskAgain {
                            logD("onNeverAskAgain : ${it.contentToString()}")
                        }

                        this.shouldShowRationale {
                            logD("shouldShowRationale : ${it.contentToString()}")
                        }

                }

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (!Settings.canDrawOverlays(context)) {
//                        val intent = Intent(Settings.WRITE_SETTINGS);
//                        startActivity(intent)
//                        return;
//                    }
//                }

            }

            1 -> {
                // <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
//                if (clipData != null && clipData.itemCount > 0) {
//                    val item = clipData.getItemAt(0)
//                    if (item != null && item.uri != null) {
//                        val contentResolver = requireContext().contentResolver
//                        val inputStream = contentResolver.openInputStream(item.uri)
//                        // 处理输入流中的图片数据
//                        if (inputStream != null) {
//                            // 使用输入流加载图片
//                            val bitmap = BitmapFactory.decodeStream(inputStream)
//                            // 在这里处理获取到的图片数据
//                            // ...
//                            inputStream.close()
//                            val img: ImageView = (requireView() as ViewGroup).children.toList()[4] as? ImageView ?: return
//                            img.setImageBitmap(bitmap)
//                        }
//                    }
//                }
                PermissionManager.nav2SettingAction(requireContext())

            }

            2->{
                if (clipData != null && clipData.itemCount > 0) {
                    val item = clipData.getItemAt(0)
                    if (item != null && item.uri != null) {
                        val contentResolver = requireContext().contentResolver

                        PermissionManager
                            .createReq(requireContext())
                            .configReq(PermissionReqOption(isDebug = true))
                            //.request(android.Manifest.permission.READ_EXTERNAL_STORAGE){
                            .request(AndroidPermission.READ_EXTERNAL_STORAGE){

//                                this.onGranted {

                                    logD("AndroidPermission.READ_EXTERNAL_STORAGE granted")

                                    val inputStream = contentResolver.openInputStream(item.uri)
                                    // 处理输入流中的图片数据
                                    if (inputStream != null) {
                                        // 使用输入流加载图片
                                        val bitmap = BitmapFactory.decodeStream(inputStream)
                                        // 在这里处理获取到的图片数据
                                        // ...
                                        inputStream.close()
                                        val img: ImageView? = (requireView() as ViewGroup).children.toList()[4] as? ImageView
                                        img?.setImageBitmap(bitmap)
                                    }
//                                }


                            }

                    }
                }
            }


            3->{

                if (clipData != null && clipData.itemCount > 0) {
                    val item = clipData.getItemAt(0)
                    if (item != null && item.uri != null) {
                        val contentResolver = requireContext().contentResolver

                        PermissionManager
                            .createReq(requireContext())
                            .configReq(PermissionReqOption(isDebug = true))
                            //.request(android.Manifest.permission.READ_EXTERNAL_STORAGE){
                            .request(AndroidPermission.ACCESS_FINE_LOCATION){

                                this.onGranted {

                                    logD("AndroidPermission.READ_EXTERNAL_STORAGE granted")

//                                    val inputStream = contentResolver.openInputStream(item.uri)
//                                    // 处理输入流中的图片数据
//                                    if (inputStream != null) {
//                                        // 使用输入流加载图片
//                                        val bitmap = BitmapFactory.decodeStream(inputStream)
//                                        // 在这里处理获取到的图片数据
//                                        // ...
//                                        inputStream.close()
//                                        val img: ImageView = (requireView() as ViewGroup).children.toList()[4] as? ImageView ?: return@onGranted
//                                        img.setImageBitmap(bitmap)
//                                    }
                                }

                                this.onNeverAskAgain {
                                    logD("onNeverAskAgain : ${it.contentToString()}")
                                }

                                this.shouldShowRationale {
                                    logD("shouldShowRationale : ${it.contentToString()}")
                                }

                            }

                    }
                }

            }


            else -> Unit
        }
    }

    override fun customChildren(): List<View> {
        val imageView = ImageView(requireContext())
        imageView.setImageResource(IconLib.android_robot_webp_720_720)
        imageView.setBackgroundColor(HexColors.GREEN_400.colorInt)
        return listOf(imageView)
    }


}
