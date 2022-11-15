package me.yangxiaobin.image_edit

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.zero_code.libEdImage.startEditImage
import me.yangxiaobin.android.kotlin.codelab.base.AbsActivity

/**
 * see https://github.com/OneZeroYang/EditImage
 */
class ImageEditActivity : AbsActivity() {

    private val imageView by lazy { findViewById<ImageView>(R.id.imgv_image_edit_activity) }

    override val contentResId: Int = R.layout.layout_image_edit_activity


    override fun afterOnCreate() {
        super.afterOnCreate()

        findViewById<View>(R.id.bt_image_edit_activity)?.setOnClickListener {
            navigateToEdit1()
        }
    }


    private fun navigateToEdit1() {
        EasyPhotos
            .createAlbum(
                this,
                false,
                true,
                GlideEngine.createGlideEngine()
            )
            //参数说明：上下文，是否显示相机按钮,是否使用宽高数据（false时宽高数据为0，扫描速度更快）
            // [配置Glide为图片加载引擎](https://github.com/HuanTanSheng/EasyPhotos/wiki/12-%E9%85%8D%E7%BD%AEImageEngine%EF%BC%8C%E6%94%AF%E6%8C%81%E6%89%80%E6%9C%89%E5%9B%BE%E7%89%87%E5%8A%A0%E8%BD%BD%E5%BA%93)
            .start(12)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 22 && resultCode == Activity.RESULT_OK) {
            //表示返回成功
            val filePath = data?.getStringExtra("data")
            filePath?.let {
                Log.d("", it)
                Glide.with(this).load(it).into(imageView)
            }

        } else if (requestCode == 12 && resultCode == Activity.RESULT_OK) {

            val resultPhotos: ArrayList<Photo> =
                data?.getParcelableArrayListExtra<Photo>(EasyPhotos.RESULT_PHOTOS) as ArrayList<Photo>


            if (resultPhotos.isNullOrEmpty()) {
                return
            }
            startEditImage(resultPhotos[0].path, 22)
        }
    }


}