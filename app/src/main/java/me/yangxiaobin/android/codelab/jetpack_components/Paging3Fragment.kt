package me.yangxiaobin.android.codelab.jetpack_components

import android.R
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingDataAdapter
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import me.yangxiaobin.android.kotlin.codelab.recyclerview.SimpleRvAdapter
import me.yangxiaobin.common_ui.EmptyFragment

class MyPagingSource() : PagingSource<Int, Int>() {

    override fun getRefreshKey(state: PagingState<Int, Int>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Int> {
        return LoadResult.Page(
            data = List(20) { it },
            prevKey = null,
            nextKey = null
        )
    }
}

class MyPagingVh(rootView: View) : RecyclerView.ViewHolder(rootView)

class MyDiffCallback : DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Int, newItem: Int): Any? {
        return super.getChangePayload(oldItem, newItem)
    }
}

class MyPagingAdapter() : PagingDataAdapter<Int, MyPagingVh>(MyDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPagingVh {
        val rootView = LayoutInflater.from(parent.context).inflate(
            R.layout.simple_list_item_1,
            parent,
            false
        )
        return MyPagingVh(rootView)
    }

    override fun onBindViewHolder(
        holder: MyPagingVh,
        position: Int
    ) {
        holder.itemView.apply {
            val tv = this.findViewById<TextView>(R.id.text1)
            val item = getItem(position)
            tv.text = "Item #$item"
        }
    }

}

@OptIn(ExperimentalPagingApi::class)
class MyRemoteMediator : RemoteMediator<Int, Int>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Int>
    ): MediatorResult {
        TODO("Not yet implemented")
    }

}

class Paging3Fragment : EmptyFragment() {

    private val pagingAdapter: MyPagingAdapter by lazy { MyPagingAdapter() }


    override fun customRootViewGroup(context: Context): ViewGroup {
        val rv = RecyclerView(requireContext())
        rv.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        rv.layoutManager = LinearLayoutManager(context)
        rv.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        val dataList = List(100) { it }
        val simpleRvAdapter = SimpleRvAdapter<Int>(
            dataList,
            R.layout.simple_list_item_1
        ) { (vh, entity, pos, payloads) ->
            val tv = vh.requireView<TextView>(R.id.text1)
            tv.text = "Item #$entity"

        }

        rv.adapter = pagingAdapter

        return rv
    }


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
        view.setBackgroundColor(Color.WHITE)
        view.isClickable = true



        viewLifecycleOwner.lifecycleScope.launch {
            val config = PagingConfig(pageSize = 20)
            val pager = Pager(config, pagingSourceFactory = { MyPagingSource() })
            pager.flow.collect {
                pagingAdapter.submitData(it)
            }
        }
    }

}