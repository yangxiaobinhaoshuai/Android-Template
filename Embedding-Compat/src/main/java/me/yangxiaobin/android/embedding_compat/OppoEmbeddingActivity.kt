package me.yangxiaobin.android.embedding_compat

import me.yangxiaobin.android.embedding_compat.databinding.ActivityOppoEmbeddingBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingActivity
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.inflater
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

class OppoEmbeddingActivity : AbsViewBindingActivity<ActivityOppoEmbeddingBinding>() {


    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "OppoEmbedding"


    override fun getActualBinding(): ActivityOppoEmbeddingBinding =
        ActivityOppoEmbeddingBinding.inflate(this.inflater)


    override fun afterOnCreate() {
        super.afterOnCreate()

        val dataList = listOf("aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg", "hhh", "iii")
        dataList.forEach {
            val tab = binding.tabLayout.newTab().setText(it + it)
            binding.tabLayout.addTab(tab)
        }

    }


}
