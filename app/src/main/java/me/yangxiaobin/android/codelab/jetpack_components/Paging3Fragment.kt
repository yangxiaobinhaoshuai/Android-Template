package me.yangxiaobin.android.codelab.jetpack_components

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingDataAdapter
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import me.yangxiaobin.android.kotlin.codelab.recyclerview.SimpleRvAdapter
import me.yangxiaobin.common_ui.EmptyFragment

class MyPagingSource() : PagingSource<Int, Int>() {

    override fun getRefreshKey(state: PagingState<Int, Int>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Int> {
        TODO("Not yet implemented")
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

class MyPagingAdapter(rootView: View) : PagingDataAdapter<Int, MyPagingVh>(MyDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPagingVh {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: MyPagingVh,
        position: Int
    ) {
        TODO("Not yet implemented")

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

    override fun customRootViewGroup(context: Context): ViewGroup {
        val rv = RecyclerView(requireContext())
        rv.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rv.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        val dataList = List(100) { it }
        rv.adapter = SimpleRvAdapter<Int>(
            dataList,
            android.R.layout.simple_list_item_1
        ) { (vh, entity, pos, payloads) ->
            val tv = vh.requireView<android.widget.TextView>(android.R.id.text1)
            tv.text = "Item #$entity"

        }
        return rv
    }


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
        view.setBackgroundColor(android.graphics.Color.WHITE)
        view.isClickable = true
    }
}