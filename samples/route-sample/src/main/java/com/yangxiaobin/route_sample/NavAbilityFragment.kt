package com.yangxiaobin.route_sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.base.ability.nav.NavAbility
import me.yangxiaobin.android.kotlin.codelab.base.ability.nav.NavArgs
import me.yangxiaobin.android.kotlin.codelab.base.ability.nav.SimpleStringNavParam
import me.yangxiaobin.android.kotlin.codelab.base.ability.nav.activityRoute
import me.yangxiaobin.android.kotlin.codelab.base.ability.nav.fragmentRoute
import me.yangxiaobin.android.kotlin.codelab.ext.intentFor
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.ButtonsFragment
import me.yangxiaobin.logger.core.LogFacade

data class EditProfileArgs(val userId: String) : NavArgs {
    override fun toBundle(): Bundle = bundleOf("userId" to userId)

    companion object : NavArgs.Factory<EditProfileArgs> {
        override fun fromBundle(bundle: Bundle): EditProfileArgs =
            EditProfileArgs(bundle.getString("userId").orEmpty())
    }
}

class NavAbilityFragment : ButtonsFragment(), NavAbility {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "RouterActivity"

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
    }

    override fun onClick(index: Int) {
        super.onClick(index)
        when (index) {
            0 -> {
                val fragmentRoute = fragmentRoute<NavArgs.NoArgs>(
                    create = { JumpTargetFragment() },
                )

                navigator.showFragmentNow(
                    fragmentRoute,
                    NavArgs.NoArgs,
                    requireView().id
                )
            }

            1 -> {
                val fragmentRoute = fragmentRoute<EditProfileArgs>(
                    create = { JumpTargetFragment() },
                )

                navigator.showFragmentNow(
                    fragmentRoute,
                    EditProfileArgs("user-123"),
                    requireView().id
                )
            }

            2 -> {
                val fragmentRoute = fragmentRoute<SimpleStringNavParam, String>(
                    create = { JumpTargetFragment() },
                    decodeResult = { it: Bundle ->
                        SimpleStringNavParam.Factory.fromBundle(it).value
                    }
                )

                lifecycleScope.launch {
                    val res: String = navigator.showFragment(
                        fragmentRoute,
                        SimpleStringNavParam("user-456)"),
                        requireView().id
                    )

                    logD("Received result from JumpTargetFragment: $res")
                }
            }

            3 -> {
                logD("Preparing to jump to JumpTargetActivity for result.")
                val activityRoute = activityRoute<SimpleStringNavParam, String>(
                    build = { context ->
                        requireContext().intentFor<JumpTargetActivity>(
                            "keeeeey" to "From NavAbilityFragment"
                        )
                    },
                    resultDecoder = { resultCode, data: Intent? ->
                        if (resultCode == Activity.RESULT_OK && data != null) {
                            data.getStringExtra("result_data") ?: "Empty Result"
                        } else {
                            "No Result"
                        }
                    }
                )

                lifecycleScope.launch {
                    val res = navigator.startActivity(
                        activityRoute,
                        SimpleStringNavParam("234234234")
                    )
                    logD("Received result from JumpTargetActivity: $res")
                }
            }
        }
    }
}