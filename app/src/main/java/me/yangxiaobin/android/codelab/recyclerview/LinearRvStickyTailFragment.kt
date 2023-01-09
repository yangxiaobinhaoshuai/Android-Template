package me.yangxiaobin.android.codelab.recyclerview

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.yangxiaobin.android.codelab.R
import me.yangxiaobin.android.codelab.databinding.FragmentLienarRvStickyTailBinding
import me.yangxiaobin.android.kotlin.codelab.base.AbsViewBindingFragment
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.context.screenSize
import me.yangxiaobin.android.kotlin.codelab.ext.context.statusBarSize
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.*
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.kotlin.codelab.recyclerview.AbsVH
import me.yangxiaobin.logger.core.LogFacade

class LinearRvStickyTailFragment : AbsViewBindingFragment<FragmentLienarRvStickyTailBinding>() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "LinearRvSticky"

    private val mockList = mutableListOf<Int>()

    private val adapter by lazy { StickyTailAdapter(mockList) }


    override fun getActualBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentLienarRvStickyTailBinding.inflate(inflater, container, false)


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        setupRecyclerView()
        setupButtons()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupButtons() {
        requireView().findViewById<Button>(R.id.add).setOnClickListener {
            mockList += mockList.lastOrNull()?.let { it + 1 } ?: 0
            adapter.notifyDataSetChanged()

        }

        requireView().findViewById<Button>(R.id.remove).setOnClickListener {
            if (mockList.isEmpty()) return@setOnClickListener
            mockList -= mockList.last()
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {

        repeat(38) { mockList += it }

        binding.rvTailFragment.adapter = this.adapter

        val lm = GridLayoutManager(requireContext(), 4)
        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount - 1) 4 else 1
            }
        }
        binding.rvTailFragment.layoutManager = lm

        binding.rvTailFragment.addOnScrollListener(object : RecyclerView.OnScrollListener() {


            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                logD("onScrollStateChanged, new :${newState.toRecyclerViewScrollStateString}.")
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                logD(
                    """
                    onScrolled
                    dx:$dx
                    dy:$dy
                    lastVisibleItem: ${lm.findLastVisibleItemPosition()}
                    lastCompleteVisibleItem: ${lm.findLastCompletelyVisibleItemPosition()}
                """.trimIndent()
                )
            }
        })

        binding.rvTailFragment.setOnItemClickListener(onLongClick = {
            logD("LinearRvSticky onLongClick pos :${it.second}")
            true
        }, onClick = {
            logD("LinearRvSticky onClick pos :${it.second}")
        })

    }

}

class StickyTailVh(v: View) : AbsVH(v)

class StickyTailAdapter(private val dataList: List<Int>) : RecyclerView.Adapter<AbsVH>() {

    private lateinit var rv: RecyclerView

    private fun logD(message: String) = Log.d("LinearRv", message)

    override fun getItemViewType(position: Int): Int {
        if (position == dataList.size) return 100
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsVH {
        rv = parent as RecyclerView

        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 100) return StickyTailVh(inflater.inflate(android.R.layout.simple_list_item_1, parent, false))
        else AbsVH(inflater.inflate(android.R.layout.simple_list_item_1, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AbsVH, position: Int) {
        holder.findView<TextView>(android.R.id.text1)?.text =
            dataList.getOrElse(position) { "Tail" }.toString() + "  " + holder.javaClass.simpleName

        if (holder is StickyTailVh) {
            // translate to bottom
            logD(
                """
                ${this.javaClass.simpleName}
                h :${holder.itemView.height}
                w :${holder.itemView.width}
                
                parent h :${rv.height}
                parent w :${rv.width}
                
                window size :$screenSize
                statusBar size :$statusBarSize
                
                rv globalVisible: ${rv.globalVisibleRect}
                rv localVisible: ${rv.localVisibleRect}
                
                item globalVisible:${holder.itemView.globalVisibleRect}
                item localVisible:${holder.itemView.localVisibleRect}
                
                item windowLocation:${holder.itemView.getWindowLocation}
                item screenLocation:${holder.itemView.getScreenLocation}
                
                hint localVisible: ${(rv.parent as View).findViewById<View>(R.id.tv_bottom_hint).localVisibleRect}
                hint visible to User :${(rv.parent as View).findViewById<View>(R.id.tv_bottom_hint).isVisibleToUser()}
                hint visible to User2 :${(rv.parent as View).findViewById<View>(R.id.tv_bottom_hint).isVisibleToUser2()}
            """.trimIndent()
            )

            holder.itemView.doOnPreDraw {
                it.updateLayoutParams<ViewGroup.LayoutParams> { this.height = 60 }
                logD(
                    """
                item preDraw windowLocation:${holder.itemView.getWindowLocation}
                item preDraw screenLocation:${holder.itemView.getScreenLocation}
            """.trimIndent()
                )
            }

        }

    }

    override fun getItemCount(): Int = dataList.size + 1


    private fun View?.isVisibleToUser(): Boolean {
        if (this == null) {
            return false
        }
        if (!this.isShown) {
            return false
        }
        val actualPosition = Rect()
        this.getGlobalVisibleRect(actualPosition)
        val screen = Rect(0, 0, screenSize.first, screenSize.second)
        return actualPosition.intersect(screen)
    }


    private fun View.isVisibleToUser2(): Boolean {
        val nodeInfo = AccessibilityNodeInfo.obtain()
        this.onInitializeAccessibilityNodeInfo(nodeInfo)
        val isVisibleToUser = nodeInfo.isVisibleToUser
        nodeInfo.recycle()
        return isVisibleToUser
    }

}
