package com.yangxiaobin.route_sample

import android.app.Activity
import android.os.Bundle
import me.yangxiaobin.android.kotlin.codelab.ext.dumpIntentExtras
import me.yangxiaobin.android.kotlin.codelab.ext.intentOf
import me.yangxiaobin.common_ui.EmptyActivity

class JumpTargetActivity : EmptyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD("JumpTargetActivity onCreate intent: ${intent.dumpIntentExtras()}")

        setResult(
            Activity.RESULT_OK,
            intentOf("result_data" to "This is the result from JumpTargetActivity")
        )
    }
}