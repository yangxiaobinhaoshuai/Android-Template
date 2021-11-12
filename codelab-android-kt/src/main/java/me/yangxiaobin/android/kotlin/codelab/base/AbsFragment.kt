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
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import me.yangxiaobin.android.kotlin.codelab.log.L
import me.yangxiaobin.kotlin.codelab.log.logD
import me.yangxiaobin.kotlin.codelab.log.logE
import me.yangxiaobin.kotlin.codelab.log.logI

@Suppress("LeakingThis")
abstract class AbsFragment : Fragment(), LogAbility {

    override val LogAbility.TAG: String
        get() = "AbsFragment:${this.javaClass.simpleName.take(11)}"

    protected abstract val layoutResId: Int

    protected val handleBackPress: Boolean = true


    private val backPressCallback by lazy {
        object : OnBackPressedCallback(handleBackPress) {
            override fun handleOnBackPressed() {
                this@AbsFragment.onBackPress()
            }
        }
    }

    init {
        logI("onConstruct ,${context?.getLogSuffix}")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logI("onAttach, context : $context ,${context.getLogSuffix}")
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressCallback)
    }

    @Deprecated("Consider beforeViewCreated or beforeViewReturned")
    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        beforeViewCreated(inflater, container, savedInstanceState)
        return inflater.inflate(layoutResId, container, false)
            .let(this::beforeViewReturned)
            .apply { this.isClickable = true }
            .also { logI("onCreateView, rootView : $it ,${context?.getLogSuffix}") }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logI("onViewCreated, view : $view, savedInstanceState: $savedInstanceState ,${context?.getLogSuffix}")
        afterViewCreated(view)
    }

    open fun afterViewCreated(view: View) {
        logI("afterViewCreated, view :$view ,${context?.getLogSuffix}")
    }

    open fun beforeViewReturned(view: View): View {
        logI("beforeViewReturned, view :$view ,${context?.getLogSuffix}")
        return view
    }

    open fun beforeViewCreated(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        logI("beforeViewCreated, bundle :$savedInstanceState ,${context?.getLogSuffix}")
    }

    open fun onBackPress() {
        logI("onBackPress ,${context?.getLogSuffix}")
        parentFragmentManager.commit { remove(this@AbsFragment) }
    }

    override fun onStart() {
        super.onStart()
        logI("onStart ,${context?.getLogSuffix}")
    }

    override fun onResume() {
        super.onResume()
        logI("onResume ,${context?.getLogSuffix}")
    }

    override fun onPause() {
        super.onPause()
        logI("onPause ,${context?.getLogSuffix}")
    }


    override fun onStop() {
        super.onStop()
        logI("onStop ,${context?.getLogSuffix}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logI("onDestroyView ,${context?.getLogSuffix}")
    }

    override fun onDestroy() {
        super.onDestroy()
        logI("onDestroy ,${context?.getLogSuffix}")
    }

    override fun onDetach() {
        super.onDetach()
        logI("onDetach ,${context?.getLogSuffix}")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        logI("onConfigurationChanged, newConfig: $newConfig ,${context?.getLogSuffix}")
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return super.onCreateAnimation(transit, enter, nextAnim)
            .also { logI("onCreateAnimation ,${context?.getLogSuffix}") }
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return super.onCreateAnimator(transit, enter, nextAnim)
            .also { logI("onCreateAnimator ,${context?.getLogSuffix}") }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logI("onLowMemory ,${context?.getLogSuffix}")
    }

}
