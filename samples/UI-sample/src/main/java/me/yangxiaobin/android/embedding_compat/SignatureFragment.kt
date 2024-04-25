package me.yangxiaobin.android.embedding_compat

import android.view.View
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.getApplicationSHA1
import me.yangxiaobin.android.kotlin.codelab.ext.getApplicationSHA256
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.SimpleTextInputFragment
import me.yangxiaobin.logger.core.LogFacade

class SignatureFragment : SimpleTextInputFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "SignatureFragment"
    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

    }

    override fun onItemClick(v: View, index: Int) {
        super.onItemClick(v, index)
        logI("onItemClick, index: $index.")
        when (index) {
            0 -> {

                val packageName = edit.text.toString()
                logI("packageName :$packageName.")
                if (packageName.isNotEmpty()) {
                    val otherSha1 = getApplicationSHA1(requireContext(), packageName)
                    logI("otherSha1 :$otherSha1.")
                    tv1.text = otherSha1
                }

                /*val signature = getApplicationSHA256(requireContext())
                logI("current app signature: $signature.")
                tv1.text = signature*/
            }

            1 -> {
                val signature = getApplicationSHA1(requireContext())
                tv2.text = signature
            }

            else -> Unit
        }
    }
}
