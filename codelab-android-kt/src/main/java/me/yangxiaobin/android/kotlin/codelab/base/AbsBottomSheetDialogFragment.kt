package me.yangxiaobin.android.kotlin.codelab.base

import android.animation.Animator
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.yangxiaobin.kotlin.codelab.ext.neatName

open class AbsBottomSheetDialogFragment : BottomSheetDialogFragment(),LogAbility {

    override val LogAbility.TAG: String
        get() = "AbsBSDF:${this.javaClass.simpleName.take(11)}"

    private val logPrefix by lazy { requireContext().getLogSuffix }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        logI("$logPrefix onAttach, context :$context")
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        logI("$logPrefix onCreateAnimation, transit :$transit,enter:$enter,nextAnim:$nextAnim")
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        logI("$logPrefix onCreateAnimator, transit :$transit,enter:$enter,nextAnim:$nextAnim")
        return super.onCreateAnimator(transit, enter, nextAnim)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logI("$logPrefix onCreate, savedInstanceState :$savedInstanceState")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logI("$logPrefix onViewCreated, view :${view.neatName},savedInstanceState:$savedInstanceState")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        logI("$logPrefix onActivityCreated, savedInstanceState :$savedInstanceState")
    }

    override fun onStart() {
        super.onStart()
        logI("$logPrefix onStart")
    }

    override fun onResume() {
        super.onResume()
        logI("$logPrefix onResume")
    }

    override fun onPause() {
        super.onPause()
        logI("$logPrefix onPause")
    }

    override fun onStop() {
        super.onStop()
        logI("$logPrefix onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logI("$logPrefix onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        logI("$logPrefix onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        logI("$logPrefix onDestroy")
    }

    override fun dismiss() {
        super.dismiss()
        logI("$logPrefix dismiss")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        logI("$logPrefix onCreateDialog,savedInstanceState:$savedInstanceState")
        return super.onCreateDialog(savedInstanceState)
    }
}
