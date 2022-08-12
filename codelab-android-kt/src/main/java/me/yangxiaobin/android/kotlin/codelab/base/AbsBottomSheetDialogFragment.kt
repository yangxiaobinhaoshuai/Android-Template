package me.yangxiaobin.android.kotlin.codelab.base

import android.animation.Animator
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.activity.addCallback
import androidx.fragment.app.commit
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.yangxiaobin.kotlin.codelab.ext.neatName

/**
 * BottomSheetDialogFragment Usage : https://juejin.cn/post/6844903465542483981
 */
open class AbsBottomSheetDialogFragment : BottomSheetDialogFragment(), LogAbility {

    override val LogAbility.TAG: String
        get() = "AbsBSDF:${this.javaClass.simpleName.take(11)}"


    override fun onAttach(context: Context) {
        super.onAttach(context)
        logI("onAttach, context :${context.neatName}.")
        requireActivity().onBackPressedDispatcher.addCallback(this) { onBackPress() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        beforeCreateView()
        val root = createRoot(inflater, container, savedInstanceState)
        afterCreateView(root)
        return root
    }

    open fun createRoot(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = null

    open fun beforeCreateView() = Unit

    open fun afterCreateView(view: View?) = Unit

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        logI("onCreateAnimation, transit :$transit,enter:$enter,nextAnim:$nextAnim")
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        logI("onCreateAnimator, transit :$transit,enter:$enter,nextAnim:$nextAnim")
        return super.onCreateAnimator(transit, enter, nextAnim)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logI("onCreate, savedInstanceState :$savedInstanceState.")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logI("onViewCreated, view :${view.neatName},savedInstanceState:$savedInstanceState.")
    }


    override fun onStart() {
        super.onStart()
        logI("onStart")
    }

    override fun onResume() {
        super.onResume()
        logI("onResume")
    }

    override fun onPause() {
        super.onPause()
        logI("onPause")
    }

    override fun onStop() {
        super.onStop()
        logI("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logI("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        logI("onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        logI("onDestroy")
    }

    override fun dismiss() {
        super.dismiss()
        logI("dismiss")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also {
            logI("onCreateDialog,savedInstanceState:$savedInstanceState,dialog:${it.neatName}")
        }
    }

    open fun onBackPress() {
        logI("onBackPress.")
        parentFragmentManager.commit { remove(this@AbsBottomSheetDialogFragment) }
    }
}
