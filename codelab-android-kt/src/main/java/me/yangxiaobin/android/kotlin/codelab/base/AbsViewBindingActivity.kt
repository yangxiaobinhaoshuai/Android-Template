package me.yangxiaobin.android.kotlin.codelab.base

import androidx.viewbinding.ViewBinding
import kotlin.properties.Delegates

abstract class AbsViewBindingActivity<T : ViewBinding> : AbsActivity() {

    override val contentResId: Int = 0

    protected var binding: T by Delegates.notNull()


    abstract fun getActualBinding(): T


    override fun setContentView(layoutResID: Int) {
        //super.setContentView(layoutResID)
        binding = getActualBinding()
        setContentView(binding.root)
    }

}