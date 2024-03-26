package me.yangxiaobin.android.kotlin.codelab.base

import android.animation.Animator
import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import androidx.activity.addCallback
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogFun
import me.yangxiaobin.android.kotlin.codelab.base.ability.ResAbility
import me.yangxiaobin.android.kotlin.codelab.ext.MatchParentParams
import me.yangxiaobin.android.kotlin.codelab.ext.context.buildResColor
import me.yangxiaobin.android.kotlin.codelab.ext.context.buildResDrawable
import me.yangxiaobin.android.kotlin.codelab.ext.context.buildResString
import me.yangxiaobin.android.kotlin.codelab.ext.uicontroller.setActivityToolBarTitle
import me.yangxiaobin.kotlin.codelab.ext.neatName

abstract class AbsFragment : Fragment(), LogAbility, ResAbility {

    override val LogAbility.TAG: String get() = "AbsFragment:${this.javaClass.simpleName.take(11)}"

    protected open val logWithPrefix: Boolean get() = false

    private val logPrefix get() = this.neatName

    override val logI: LogFun
            = fun(message: String)
            = super.logI(if (logWithPrefix) "$logPrefix -> $message" else message)

    protected open val layoutResId: Int = 0

    /**
     * Whether to handle back event.
     */
    protected open val handleBackPress = true

    override val int2Color: Int.() -> Int by lazy { buildResColor(requireContext()) }
    override val int2String: Int.() -> String by lazy { buildResString(requireContext()) }
    override val int2Drawable: Int.() -> Drawable by lazy { buildResDrawable(requireContext()) }

    init {
        @Suppress("LeakingThis")
        logI("onConstruct")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logI("onAttach, context : ${context.neatName}.")
        requireActivity().onBackPressedDispatcher.addCallback(this, enabled = handleBackPress) { onBackPress() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logI("onCreate, savedInstanceState : $savedInstanceState getArguments :${arguments}.")
    }

    @Deprecated("Consider beforeViewCreated or beforeViewReturned")
    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val rootView = createRootView(inflater, container, savedInstanceState)
            ?: if (layoutResId <= 0) return super.onCreateView(inflater,
                container,
                savedInstanceState)
            else inflater.inflate(layoutResId, container, false)

        beforeViewCreated(inflater, container, savedInstanceState)

        return rootView
            .let(this::beforeViewReturned)
            .apply { this.isClickable = true }
            .also { logI("onCreateView, rootView : ${it.neatName}.") }
    }

    protected open fun createRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = null

    /**
     * Example for createRootView
     */
    protected fun createEmptyFrameLayout(): ViewGroup = FrameLayout(requireContext()).apply { this.layoutParams = MatchParentParams }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logI("onViewCreated, view : ${view.neatName}, savedInstanceState: $savedInstanceState .")
        afterViewCreated(view)
    }

    open fun afterViewCreated(view: View) {
        logI("afterViewCreated, view :${view.neatName}.")
        setActivityToolBarTitle(TAG)
    }

    open fun beforeViewReturned(view: View): View {
        logI("beforeViewReturned, view :${view.neatName}.")
        return view
    }

    open fun beforeViewCreated(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) {
        logI("beforeViewCreated, bundle :$savedInstanceState.")
    }

    open fun onBackPress() {
        logI("onBackPress.")
        parentFragmentManager.commit { remove(this@AbsFragment) }
    }

    override fun onStart() {
        super.onStart()
        logI("onStart.")
    }

    override fun onResume() {
        super.onResume()
        logI("onResume.")
    }

    override fun onPause() {
        super.onPause()
        logI("onPause.")
    }


    override fun onStop() {
        super.onStop()
        logI("onStop.")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logI("onDestroyView.")
    }

    override fun onDestroy() {
        super.onDestroy()
        logI("onDestroy.")
    }

    override fun onDetach() {
        super.onDetach()
        logI("onDetach.")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        logI("onConfigurationChanged, newConfig: $newConfig.")
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return super.onCreateAnimation(transit, enter, nextAnim)
            .also { logI("onCreateAnimation.") }
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        return super.onCreateAnimator(transit, enter, nextAnim)
            .also { logI("onCreateAnimator.") }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        logI("onLowMemory.")
    }

}
