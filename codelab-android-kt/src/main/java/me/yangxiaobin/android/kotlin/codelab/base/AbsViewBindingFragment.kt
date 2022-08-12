package me.yangxiaobin.android.kotlin.codelab.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class AbsViewBindingFragment<T : ViewBinding> : AbsFragment() {

    private var _binding: T? = null
    protected val binding: T by lazy { requireNotNull(_binding){"Can't access binding before onCreateView or after onDestroyView"} }

    abstract fun getActualBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): T

    override fun createRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = getActualBinding(inflater, container, savedInstanceState)
        _binding?.root?.setBackgroundColor(Color.WHITE)
        _binding?.root?.isClickable = true
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
