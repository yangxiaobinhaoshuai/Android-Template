package me.yangxiaobin.image_edit.lib.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.yangxiaobin.image_edit.R
import me.yangxiaobin.image_edit.lib.EditImageView
import me.yangxiaobin.image_edit.lib.adapter.BaseAdapter
import me.yangxiaobin.image_edit.lib.core.EditImageMode
import me.yangxiaobin.image_edit.lib.loadBitmap
import me.yangxiaobin.image_edit.lib.saveBitMap
import me.yangxiaobin.image_edit.lib.setChangeListener
import me.yangxiaobin.image_edit.lib.ui.text.EditImageText
import me.yangxiaobin.image_edit.lib.util.StatusBarUtil
import me.yangxiaobin.image_edit.lib.view.EditColorGroup
import me.yangxiaobin.image_edit.lib.view.EditImageToolsBean
import me.yangxiaobin.image_edit.lib.view.EditMosaicGroup
import me.yangxiaobin.image_edit.lib.view.EditMosaicGroup.*


/**
 * 编辑图片activity
 * @author ZeroCode
 * @date 2021/5/8 : 11:09
 *
 *1. Activity 需要处理标题栏和状态栏
 */

//TODO 1.剪裁模块，小概率还原得时候图片定位不在中心点得问题，会导致旋转中心也不会在中心点得位置，目前触发机制还不明确


private const val checkPermissionCode: Int = 0xf11

class EditImageActivity : AppCompatActivity() {
    /**
     * 资源
     */
    private val src: String? by lazy { intent.getStringExtra("data") }

    /**
     * 选中的工具下标
     */
    private var selectedToolsIndex: Int = -1

    /**
     * 工具数据
     */
    private val toolsList = arrayListOf(
        /**
         *  画笔
         */
        EditImageToolsBean(intArrayOf(R.drawable.btn_ed_freed, R.drawable.btn_ed_free)),
        /**
         * 马赛克
         */
        EditImageToolsBean(intArrayOf(R.drawable.btn_ed_mosaiced, R.drawable.btn_ed_mosaic)),
        /**
         * 剪裁
         */
        EditImageToolsBean(intArrayOf(R.drawable.btn_ed_cuted, R.drawable.btn_ed_cut)),
        /**
         * 文字
         */
        EditImageToolsBean(intArrayOf(R.drawable.btn_ed_texted, R.drawable.btn_ed_text)),
        /**
         * 箭头
         */
        EditImageToolsBean(intArrayOf(R.drawable.btn_ed_arrowed, R.drawable.btn_ed_arrow))
    )


    private val mIMGView: EditImageView by lazy { findViewById<EditImageView>(R.id.mIMGView) }
    private val cg_colors: EditColorGroup by lazy { findViewById<EditColorGroup>(R.id.cg_colors) }
    private val cg_mosaic: EditMosaicGroup by lazy { findViewById<EditMosaicGroup>(R.id.cg_mosaic) }
    private val cg_arrows: EditMosaicGroup by lazy { findViewById<EditMosaicGroup>(R.id.cg_arrows) }

    /**
     * 编辑工具的适配器
     */
    private val toolsAdapter: BaseAdapter<EditImageToolsBean> by lazy {
        BaseAdapter<EditImageToolsBean>(R.layout.item_edit_image_tools_layout, toolsList)
            .apply {
                onBind { itemBinding: View, position: Int, data: EditImageToolsBean ->

                    itemBinding.findViewById<ImageView>(R.id.item_tools_image)
                        ?.setImageResource(data.getImage())


                    itemBinding.setOnClickListener {
                        if (cg_colors.visibility == View.VISIBLE) cg_colors.visibility = View.GONE
                        if (cg_mosaic.visibility == View.VISIBLE) cg_mosaic.visibility = View.GONE
                        if (cg_arrows.visibility == View.VISIBLE) cg_arrows.visibility = View.GONE
                        findViewById<View>(R.id.ed_tools)?.visibility = View.VISIBLE

                        when (position) {
                            // 涂鸦
                            0 -> {
                                mIMGView.mode = EditImageMode.DOODLE
                                cg_colors.visibility = View.VISIBLE
                                if (cg_colors.checkedRadioButtonId == null) cg_colors.checkColor =
                                    resources.getColor(R.color.image_color_red)
                            }
                            // 马赛克
                            1 -> {
                                mIMGView.mode = EditImageMode.MOSAIC
                                cg_mosaic.visibility = View.VISIBLE
                                if (cg_mosaic.checkSize == IMG_MOSAIC_SIZE_NO) cg_mosaic.checkSize =
                                    IMG_MOSAIC_SIZE_1
                            }
                            // 剪裁
                            2 -> {
                                mIMGView.mode = EditImageMode.CLIP
                                findViewById<View>(R.id.ed_tools)?.visibility = View.GONE
                                findViewById<View>(R.id.clip_view)?.visibility = View.VISIBLE
//                            mIMGView?.resetClip()
                            }
                            // 文字
                            3 -> {
                                mIMGView.mode = EditImageMode.NONE
                                mIMGView.addStickerText(EditImageText("文字", Color.RED))
                            }
                            // 箭头
                            4 -> {
                                mIMGView.mode = EditImageMode.ARROWS
                                cg_arrows.visibility = View.VISIBLE
                                if (cg_arrows.checkSize == IMG_MOSAIC_SIZE_NO) cg_arrows.checkSize =
                                    IMG_MOSAIC_SIZE_1
                            }
                        }
                        if (selectedToolsIndex != -1) toolsList[selectedToolsIndex].isSelected =
                            false
                        data.isSelected = true
                        selectedToolsIndex = position
                        notifyDataSetChanged()
                    }
                }
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image)
        if (src.isNullOrBlank()) throw RuntimeException("请指定资源和返回标识！")
        checkPermission()
    }


    fun complete(view: View) {
        val saveBitmap = mIMGView.saveBitmap()
        val path = saveBitMap(
            bitmap = saveBitmap,
            name = System.currentTimeMillis().toString(),
            description = "batchat_app_image"
        )
        if (path.isBlank()) throw java.lang.RuntimeException("保存文件错误！")

        saveBitmap.recycle()
        intent.putExtra("data", path)
        setResult(Activity.RESULT_OK, intent)
        finish()


    }

    fun cancel(view: View) {
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }


    /**
     * 检查权限
     */
    private fun checkPermission() {
        //申请权限（Android6.0及以上 需要动态申请危险权限）
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), checkPermissionCode
            )
        } else {
            initView()
        }
    }

    /**
     * 权限申请回掉
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == checkPermissionCode) {
            if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initView()
            } else {
                Toast.makeText(this, "权限已拒绝", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }


    /**
     * 初始化View
     */
    private fun initView() {
        //初始化状态栏
        changeStatusBarColor(resources.getColor(R.color.ed_image_title_bar_bg_color), false)
        //初始化工具栏
        findViewById<RecyclerView>(R.id.edit_image_tools_list)?.apply {
            layoutManager =
                LinearLayoutManager(this@EditImageActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = toolsAdapter
        }
        //适配状态兰入侵
        findViewById<View>(R.id.status_bar)?.apply {
            val lp = layoutParams
            lp.height = StatusBarUtil.getStatusBarHeight(context)
            layoutParams = lp
        }
        //初始化编辑View

        val bitmap = loadBitmap(src!!)
        mIMGView.setImageBitmap(bitmap)

        //设置涂鸦颜色选择监听
        cg_colors.setChangeListener {
            mIMGView.setPenColor(cg_colors.checkColor)
        }

        //设置马赛克大小
        cg_mosaic.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            mIMGView.setMosaicWidth(cg_mosaic.checkSize)
        }

        //设置箭头大小
        cg_arrows.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            when (cg_arrows.checkSize) {
                IMG_MOSAIC_SIZE_1 -> mIMGView.setArrowsSize(1)
                IMG_MOSAIC_SIZE_2 -> mIMGView.setArrowsSize(2)
                IMG_MOSAIC_SIZE_3 -> mIMGView.setArrowsSize(3)
                IMG_MOSAIC_SIZE_4 -> mIMGView.setArrowsSize(4)
                IMG_MOSAIC_SIZE_5 -> mIMGView.setArrowsSize(5)
            }
        }
        //设置返回上一步
        findViewById<View>(R.id.edit_image_last_step)?.setOnClickListener {
            val mode: EditImageMode? = mIMGView.mode
            if (mode === EditImageMode.DOODLE) {
                mIMGView.undoDoodle()
            } else if (mode === EditImageMode.MOSAIC) {
                mIMGView.undoMosaic()
            } else if (mode == EditImageMode.ARROWS) {
                mIMGView.clearLastArrows()
            }
        }

        //初始化 剪裁相关得View
        initClipView()


    }


    override fun onDestroy() {
        mIMGView.getImage()?.recycle()
        System.gc()
        System.gc()
        super.onDestroy()
    }

    /**
     * 初始化 剪裁相关得View
     */
    private fun initClipView() {

    }


    /**
     * 改变状态栏颜色
     * @param color
     * @param isCilp 是否需要padding状态栏高度，如果需要自己实现状态栏逻辑就传入false
     * @param dl 如果要兼容DrawerLayout则传入
     */
    private fun changeStatusBarColor(
        @ColorInt color: Int,
        isCilp: Boolean = true,
        dl: androidx.drawerlayout.widget.DrawerLayout? = null
    ) {
        //如果dl不为空则都使用半透明，因为dl可能拉出白色背景
        if (dl != null) {
            StatusBarUtil.setStatusBarLightMode(this, false)
            StatusBarUtil.setColorTranslucentForDrawerLayout(this, dl, color)
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果版本号大于等于M，则必然可以修改状态栏颜色
            StatusBarUtil.setColor(this, color, isCilp)
            StatusBarUtil.setStatusBarLightModeByColor(this, color)
            return
        }
        //这里处理的是版本号低于M的系统
        //判断设置的颜色是深色还是浅色，然后设置statusBar的文字颜色
        val status = StatusBarUtil.setStatusBarLightModeByColor(this, color)
        //fixme 如果手机机型不能改状态栏颜色就不允许开启沉浸式,如果业务需求请修改代码逻辑
        if (!status) {//如果状态栏的文字颜色改变失败了则设置为半透明
            StatusBarUtil.setColorTranslucent(this, color, isCilp)
        } else {//如果状态栏的文字颜色改变成功了则设置为全透明
            StatusBarUtil.setColor(this, color, isCilp)
            //改变了状态栏后需要重新设置一下状态栏文字颜色
            StatusBarUtil.setStatusBarLightModeByColor(this, color)
        }

    }


}