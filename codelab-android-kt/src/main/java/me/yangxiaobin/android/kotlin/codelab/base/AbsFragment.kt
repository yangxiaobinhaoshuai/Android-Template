package me.yangxiaobin.android.kotlin.codelab.base

import android.animation.Animator
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import me.yangxiaobin.android.kotlin.codelab.log.L
import me.yangxiaobin.kotlin.codelab.log.logD
import me.yangxiaobin.kotlin.codelab.log.logE
import me.yangxiaobin.kotlin.codelab.log.logI

abstract class AbsFragment : Fragment() {

    open val AbsFragment.TAG: String
        get() = "AbsFragment:${this.javaClass.simpleName.take(11)}"

    protected val logI by lazy { L.logI(TAG) }
    protected val logD by lazy { L.logD(TAG) }
    protected val logE by lazy { L.logE(TAG) }

    protected abstract val layoutResId: Int

    open val handleBackPress: Boolean = true

    private val backPressCallback by lazy {
        object : OnBackPressedCallback(handleBackPress) {
            override fun handleOnBackPressed() {
                this@AbsFragment.onBackPress()
            }
        }
    }

    init {
        logI("onConstruct")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logI("onAttach, context : $context")
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        beforeViewCreated()
        return inflater.inflate(layoutResId, container, false)
            .let(this::beforeViewReturned)
            .apply { this.isClickable = true }
            .also { logI("onAttach, rootView : $it") }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logI("onViewCreated, view : $view, savedInstanceState: $savedInstanceState")
        afterViewCreated(view)
    }

    open fun afterViewCreated(view: View) {
        logI("afterViewCreated, view :$view")
    }

    open fun beforeViewReturned(view: View): View {
        logI("beforeViewReturned, view :$view")
        return view
    }

    open fun beforeViewCreated() {
        logI("beforeViewCreated")
    }

    open fun onBackPress() {
        logI("onBackPress")
        parentFragmentManager.commit { remove(this@AbsFragment) }
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
        logI("onDetach")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        logI("onConfigurationChanged, newConfig: $newConfig")
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return super.onCreateAnimation(transit, enter, nextAnim).also { logI("onCreateAnimation") }
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return super.onCreateAnimator(transit, enter, nextAnim).also { logI("onCreateAnimator") }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logI("onLowMemory")
    }

}
