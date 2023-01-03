package me.yangxiaobin.android.codelab.touch_event

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onEach
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.launchIn
import me.yangxiaobin.android.kotlin.codelab.ext.uicontroller.showFragmentToast
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyFragment
import me.yangxiaobin.logger.core.LogFacade

@SuppressLint("ClickableViewAccessibility")
class ContinuousClickFragment : EmptyFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "ContinuousClick"


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        view as ViewGroup
        val firstBt = view.children.toList()[0]
        val secondBt = view.children.toList()[1]

        setupIntuitiveDialog(firstBt)
        setupFlowEventDialog(secondBt)
    }

    private fun showAlertDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Workspace Debug Setting")
            .setItems(
                arrayOf(
                    "Reset Recommend Dialog",
                    "Reset Resent Conversion Toast",
                    "Cancel"
                )
            ) { dialog, which ->
                when (which) {
                    0 -> showFragmentToast("Click first")

                    1 -> showFragmentToast("Click Second")

                    else -> Unit
                }

                dialog.dismiss()

            }
            .setCancelable(false)
            .show()
    }

    private fun setupIntuitiveDialog(firstBt: View) {
        var count = 0
        var lastModifyTimestamp = -1L

        firstBt.setOnTouchListener { _, event ->

            if (count >= 5) {
                showFragmentToast("DebugSetting start")
                count = 0
                lastModifyTimestamp = -1L
                showAlertDialog()
                return@setOnTouchListener false
            }

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    logD("$lastModifyTimestamp , $count")

                    if (lastModifyTimestamp > 0 && System.currentTimeMillis() - lastModifyTimestamp > 500) {
                        count = 0
                        return@setOnTouchListener false
                    }

                    count += 1
                }
                else -> Unit
            }

            lastModifyTimestamp = System.currentTimeMillis()
            false
        }

    }

    private fun setupFlowEventDialog(bt: View) {

        callbackFlow {
            var lastTimestamp = -1L
            var count = 0

            fun reset() {
                count = 0
                lastTimestamp = -1
            }

            bt.setOnTouchListener { _, event ->

                if (count >= 5) {
                    this.trySend(Unit)
                    reset()
                }

                val curTimestamp = System.currentTimeMillis()

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        logD("flow event: $lastTimestamp , $count")
                        if (lastTimestamp > 0 && curTimestamp - lastTimestamp < 500) {
                            count += 1
                        } else reset()
                    }
                    else -> Unit
                }
                lastTimestamp = curTimestamp
                false
            }

            awaitClose()

        }.onEach {
            logD("Click 5 times --->")
            showAlertDialog()
        }.launchIn(lifecycleScope)
    }

}
