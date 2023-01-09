package me.yangxiaobin.android.embedding_compat

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import me.yangxiaobin.android.embedding_compat.databinding.ActivityOppoEmbeddingBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingActivity
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.context.inflater
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.logger.core.LogFacade

/**
 * Oppo 折叠屏适配 WIP
 */
class OppoEmbeddingActivity : AbsViewBindingActivity<ActivityOppoEmbeddingBinding>() {


    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "OppoEmbedding"

    private val dataList = listOf("aaa", "bbb")


    override fun getActualBinding(): ActivityOppoEmbeddingBinding =
        ActivityOppoEmbeddingBinding.inflate(this.inflater)


    override fun afterOnCreate() {
        super.afterOnCreate()

        setupTabLayout()
        setupViewPager()
        connectTabLayoutAndViewPager()
    }

    private fun setupTabLayout() {

        dataList.forEach {
            val tab = binding.tabLayout.newTab().setText(it + it)
            binding.tabLayout.addTab(tab)
        }

    }

    private fun setupViewPager() {

        val fragments = listOf(FragmentA(), FragmentB())

        binding.viewPager2.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount(): Int = fragments.size

            override fun createFragment(position: Int): Fragment = fragments[position]

        }

    }

    private fun connectTabLayoutAndViewPager() {
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager2
        ) { tab, position ->

        }.attach()
    }


}
