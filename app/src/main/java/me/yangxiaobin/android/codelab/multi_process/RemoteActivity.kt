package me.yangxiaobin.android.codelab.multi_process

import me.yangxiaobin.android.codelab.R
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.base.AbsActivity

class RemoteActivity : AbsActivity() {

    override val LogAbility.TAG: String get() = "Sample-app"

    override val contentResId: Int = R.layout.activity_remote
}
