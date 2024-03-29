package me.yangxiaobin.android.kotlin.codelab.base

import androidx.viewbinding.ViewBinding
import kotlin.properties.Delegates

@Suppress("MemberVisibilityCanBePrivate")
abstract class AbsViewBindingActivity<T : ViewBinding> : AbsActivity() {

    /**
     * MUST be great than 0
     */
    override val contentResId = 1

    protected var binding: T by Delegates.notNull()


    abstract fun getActualBinding(): T


    override fun setContentView(layoutResID: Int) {
        //super.setContentView(layoutResID)
        binding = getActualBinding()
        setContentView(binding.root)
    }

}
