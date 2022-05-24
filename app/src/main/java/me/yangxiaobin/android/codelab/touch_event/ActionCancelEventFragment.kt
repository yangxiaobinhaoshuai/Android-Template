package me.yangxiaobin.android.codelab.touch_event

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.postDelayed
import androidx.core.view.setPadding
import me.yangxiaobin.android.codelab.common.EmptyFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.getActionString
import me.yangxiaobin.android.kotlin.codelab.ext.showFragmentToast
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class ActionCancelEventFragment : EmptyFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "ActionCancelFragment"

    override val buttonsCount: Int
        get() = 1

    override fun customRootViewGroup(context: Context): ViewGroup = MyFrameLayout(context)

    override fun customChildren(): List<View> = listOf(
        /**
         * <style name="Widget.Button">
         *   <item name="android:background">@android:drawable/btn_default</item>
         *   <item name="android:focusable">true</item>
         *   <item name="android:clickable">true</item>
         *   <item name="android:textAppearance">?android:attr/textAppearanceSmallInverse</item>
         *   <item name="android:textColor">@android:color/primary_text_light</item>
         *   <item name="android:gravity">center_vertical|center_horizontal</item>
         * </style>
         *
         *
         *
         * <selector xmlns:android="http://schemas.android.com/apk/res/android">
         *   <item android:state_window_focused="false" android:state_enabled="true" android:drawable="@drawable/btn_default_normal" />
         *   <item android:state_window_focused="false" android:state_enabled="false" android:drawable="@drawable/btn_default_normal_disable" />
         *   <item android:state_pressed="true" android:drawable="@drawable/btn_default_pressed" />
         *   <item android:state_focused="true" android:state_enabled="true" android:drawable="@drawable/btn_default_selected" />
         *   <item android:state_enabled="true" android:drawable="@drawable/btn_default_normal" />
         *   <item android:state_focused="true" android:drawable="@drawable/btn_default_normal_disable_focused" />
         *   <item android:drawable="@drawable/btn_default_normal_disable" />
         * </selector>
         */
        MyButton(requireContext(), defStyleAttr = android.R.attr.buttonStyle),
        SubFrameLayout(requireContext()).apply {
            this.doOnPreDraw {
                (this.layoutParams as FrameLayout.LayoutParams).apply {
                    this.width = 600
                    this.height = 200
                }
                this.setBackgroundColor(Color.BLUE)
                this.setLogTag("MyViewGroup")
            }
        },
        SubFrameLayout(requireContext()).apply {
            this.doOnPreDraw {
                (this.layoutParams as FrameLayout.LayoutParams).apply {
                    this.width = 600
                    this.height = 200
                }
                this.setBackgroundColor(Color.BLUE)
                this.setLogTag("MyViewGroup")
            }


            this.addView(MyButton(requireContext()).apply {
                this.setLogTag("outGroup")
                this.doOnPreDraw {
                    (this.layoutParams as FrameLayout.LayoutParams).apply {
                        this.width = 300
                        this.height = 100
                    }
                    this.setBackgroundColor(Color.GREEN)
                    this.setLogTag("innerButton")
                }
            })
        }
    )


    @SuppressLint("SetTextI18n", "AppCompatCustomView")
    inner class MyButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : Button(context, attrs, defStyleAttr) {

        private var logTag = "RootFrameLayout"

        init {
            this.text = "MyButton."
            this.setPadding(60)

//            this.setTextColor(Color.WHITE)
//            this.setBackgroundColor(Color.RED)

            this.setOnClickListener {
                showFragmentToast("MyButton clicked.")
                logI("$logTag, MyButton clicked.")
            }
        }

        fun setLogTag(tag:String) = apply {
            this.logTag = tag
        }

        override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
            return super.dispatchTouchEvent(event).also { logI("$logTag, MyButton, dispatchTouchEvent: $it, e:${event.getActionString}") }
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouchEvent(event: MotionEvent?): Boolean {
            return super.onTouchEvent(event).also { logI("$logTag, MyButton, onTouchEvent:$it, e:${event.getActionString}, ===> ${this.isFocused}") }
        }

    }

    open inner class MyFrameLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
    ) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

        protected var logTag = "RootFrameLayout"

        fun setLogTag(tag:String) = apply {
            this.logTag = tag
        }

        override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
            return super.dispatchTouchEvent(event).also { logI("$logTag, dispatchTouchEvent:$it, e:${event.getActionString}") }
        }

        override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
            return super.onInterceptTouchEvent(event).also {  logI("$logTag, dispatchTouchEvent:$it, e:${event.getActionString}") }
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouchEvent(event: MotionEvent?): Boolean {
            return super.onTouchEvent(event).also { logI("$logTag, onTouchEvent:$it, e:${event.getActionString}") }
        }
    }

    inner class SubFrameLayout(context: Context) : MyFrameLayout(context){

        init {
            this.isClickable = true
        }

        override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
            return super.dispatchTouchEvent(event).also { logI("$logTag, dispatchTouchEvent:$it, e:${event.getActionString}") }
        }

        override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
            // 会触发 child action_cancel.
            //if (event?.action == MotionEvent.ACTION_MOVE) return true
            return super.onInterceptTouchEvent(event).also {  logI("$logTag, dispatchTouchEvent:$it, e:${event.getActionString}") }
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouchEvent(event: MotionEvent?): Boolean {
            return super.onTouchEvent(event).also { logI("$logTag, onTouchEvent:$it, e:${event.getActionString}") }
        }
    }



}
