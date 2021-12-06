package me.yangxiaobin.android.kotlin.codelab.ext

import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


/**
 * Window inset references :
 *
 * Becoming a master window fitter : https://www.youtube.com/watch?v=_mGDMVRO3iE
 *
 * 处理视觉冲突 | 手势导航 (二) :https://juejin.cn/post/6844904006343458830
 */

/**
 * 获取状态栏高度
 */
val Window.getStatusBarHeight: Int?
    get() = ViewCompat.getRootWindowInsets(this.decorView)
        ?.getInsets(WindowInsetsCompat.Type.statusBars())
        ?.top

/**
 * 当 System bar 隐藏时 getInsets() 获取的高度为 0，如果想在隐藏状态时也能获取高度，可以使用 getInsetsIgnoringVisibility() 方法
 */
val Window.getStatusBarHeightWhenHidden: Int?
    get() = ViewCompat.getRootWindowInsets(this.decorView)
        ?.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.statusBars())
        ?.top

/**
 * 获取底部导航栏高度
 */
val Window.getNavigatorBarHeight: Int?
    get() = ViewCompat.getRootWindowInsets(this.decorView)
        ?.getInsets(WindowInsetsCompat.Type.statusBars())
        ?.bottom


/**
 * 在导航栏被隐藏时，获取导航栏高度
 */
val Window.getNavigatorBarHeightWhenHidden: Int?
    get() = ViewCompat.getRootWindowInsets(this.decorView)
        ?.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.statusBars())
        ?.bottom



/**
 * TODO ：待验证
 * 获取软键盘高度
 */
val Window.getImeHeight: Int?
    get() = ViewCompat.getRootWindowInsets(this.decorView)
        ?.getInsets(WindowInsetsCompat.Type.ime())
        ?.top

/**
 * TODO ：待验证
 * 当软键盘被隐藏时，获取软键盘高度
 */
val Window.getImeHeightWhenHidden: Int?
    get() = ViewCompat.getRootWindowInsets(this.decorView)
        ?.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.ime())
        ?.top

