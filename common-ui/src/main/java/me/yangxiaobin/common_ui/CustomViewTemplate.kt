package me.yangxiaobin.common_ui

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * Kt 自定义 View 构造器模板
 */

class CustomViewTemplate1 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr)


class CustomViewTemplate2 : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}
