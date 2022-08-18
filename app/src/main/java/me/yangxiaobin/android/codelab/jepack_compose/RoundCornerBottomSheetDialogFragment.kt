package me.yangxiaobin.android.codelab.jepack_compose

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.doOnTextChanged
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.yangxiaobin.android.codelab.R
import me.yangxiaobin.android.kotlin.codelab.base.AbsBottomSheetDialogFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.androidColor
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.common_ui.EmptyActivity
import me.yangxiaobin.logger.core.LogFacade
import org.jetbrains.anko.intentFor

class RoundCornerBottomSheetDialogFragment: AbsBottomSheetDialogFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "BottomSheetDialog"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = hybridContentView()


    /**
     * 解决圆角白边，或者在这里创建 dialog 时候传入 windowBackground:transparent 的主题
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
            .also {

                disableSwipeDownDismiss(it)

                //interceptBackPress(it)
            }
    }

    /**
     * 因为圆角会有白边，所以把 dialog window 背景设为透明
     */
    //override fun getTheme(): Int = super.getTheme()
    override fun getTheme(): Int = R.style.TransparentDialogTheme

    override fun onStart() {
        super.onStart()
        makeBehaviorExpand()
    }

    /**
     *  BottomSheetDialog 默认是 collapsed 的状态，可能会展示不全，需要向上拖动一下，改为 expand 即可
     */
    private fun makeBehaviorExpand(){
        val parent: ViewParent = view?.parent ?: return
        BottomSheetBehavior.from(parent as View).state = BottomSheetBehavior.STATE_EXPANDED
    }

    /**
     * 禁止下拉拖拽消失
     */
    private fun disableSwipeDownDismiss(d: Dialog) {

        val behaviour = (d as BottomSheetDialog).behavior
        behaviour.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    /**
     *  可能 back 会取消 fragment 进而 dismiss bottomSheetDialog, 所以这个逻辑按需处理
     */
    private fun interceptBackPress(d: Dialog) {
        d.setOnKeyListener { _, keyCode, event ->

            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {

                logD("Create new todo intercepted back event.")

                dismiss()

                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }
    }

    private fun hybridContentView() = ComposeView(requireContext())
        .apply {
            setContent {
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .height(644.dp),
                    color = Color.Magenta,
                    shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp),
                ) {
                    Column {
                        AndroidEditTextView()
                        ComposeOperatorBar()
                    }
                }

            }
        }

    @Composable
    private fun AndroidEditTextView() = AndroidView(
        factory = {

            val edt = layoutInflater.inflate(R.layout.edit_text_panel, view as ViewGroup, false)

            edt.findViewById<EditText>(R.id.edt_bottom_sheet_dialog)
                .apply { this?.setTextColor(androidColor.BLACK) }
                .apply { this?.setHintTextColor(androidColor.BLACK) }
                .doOnTextChanged { text: CharSequence?,
                                   start: Int,
                                   _: Int,
                                   count: Int ->

                    if (count == 1 && text?.get(start) == '@') {
                        // startFragment
                        //requireActivity().navigateToFragment(EmptyFragment())
                        startActivity(requireContext().intentFor<EmptyActivity>())
                    }
                }

            edt

        },

        update = {

        }
    )

    @Composable
    private fun ComposeOperatorBar() = Row(
        modifier = Modifier
    ) {

        Text("时间")

        Text("执行人")

        Text("发送")
    }

}
