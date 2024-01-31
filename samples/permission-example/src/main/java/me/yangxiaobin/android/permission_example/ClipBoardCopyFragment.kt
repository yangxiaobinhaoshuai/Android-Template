package me.yangxiaobin.android.permission_example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.children
import me.yangxiaobin.android.android_icons.IconLib
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.uicontroller.showFragmentToast
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

                logD("clipData: $clipData, clipData.itemCount: ${clipData?.itemCount}")

                if (clipData != null && clipData.itemCount > 0) {
                    val text: CharSequence? = clipData.getItemAt(0).text
                    logD("text: $text")
                    // 处理剪贴板上的文本内容
                    if (text != null) {
                        // 打印剪贴板上的文本内容
                        logD("Clipboard: $text")
                        showFragmentToast("Toast Clipboard: $text")
                    }
                }
            }

            1 -> {
                // <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
                if (clipData != null && clipData.itemCount > 0) {
                    val item = clipData.getItemAt(0)
                    if (item != null && item.uri != null) {
                        val contentResolver = requireContext().contentResolver
                        val inputStream = contentResolver.openInputStream(item.uri)
                        // 处理输入流中的图片数据
                        if (inputStream != null) {
                            // 使用输入流加载图片
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            // 在这里处理获取到的图片数据
                            // ...
                            inputStream.close()
                            val img: ImageView = (requireView() as ViewGroup).children.toList()[4] as? ImageView ?: return
                            img.setImageBitmap(bitmap)
                        }
                    }
                }

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
                            .request(AndroidPermission.READ_EXTERNAL_STORAGE){

                                this.onGranted {

                                    logD("AndroidPermission.READ_EXTERNAL_STORAGE granted")

                                    val inputStream = contentResolver.openInputStream(item.uri)
                                    // 处理输入流中的图片数据
                                    if (inputStream != null) {
                                        // 使用输入流加载图片
                                        val bitmap = BitmapFactory.decodeStream(inputStream)
                                        // 在这里处理获取到的图片数据
                                        // ...
                                        inputStream.close()
                                        val img: ImageView = (requireView() as ViewGroup).children.toList()[4] as? ImageView ?: return@onGranted
                                        img.setImageBitmap(bitmap)
                                    }
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
