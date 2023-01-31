package me.yangxiaobin.image_edit.lib.adapter

import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * 通用Adapter
 * Created by Extends on 2017/3/23.
 */

open class BaseAdapter<T>(
    @LayoutRes layoutId: Int,
    dataList: ArrayList<T>,
    isStandard: Boolean = true
) : BaseRecyclerAdapter<T, BaseAdapter.Holder>(),
    ItemTouchHelperAdapter {
    private var layoutId: Int = 0
    private var isStandard: Boolean = true
    private var if_onBind: OnBind<T>? = null
    private var mPresenter: Presenter? = null

    override fun onCreate(parent: ViewGroup?, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent?.context).inflate(layoutId, parent, false))
    }

    override fun onBind(viewHolder: Holder, realPosition: Int, data: T) {
        if (isStandard) {
            (viewHolder.itemView).apply {
//                setVariable(BR.item, data)
//                setVariable(BR.presenter, getPresenter())
//                setVariable(BR.position, realPosition)
            }
        }
        if_onBind?.onBind(viewHolder.itemView, realPosition, data)
    }

    fun setOnBind(if_onBind: OnBind<T>) {
        this.if_onBind = if_onBind
    }

    fun onBind(l: (itemRoot: View, position: Int, data: T) -> Unit) {
        setOnBind(object : OnBind<T> {
            override fun onBind(itemRoot: View, position: Int, data: T) {
                l(itemRoot, position, data)
            }
        })
    }

    fun setPresenter(presenter: Presenter) {
        mPresenter = presenter
    }

    protected fun getPresenter(): Presenter? {
        return mPresenter
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view)

    var onItemMove: ((fromPosition: Int, toPosition: Int) -> Unit)? = null

    var onItemDissmiss: ((position: Int) -> Unit)? = null

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(datas, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        onItemMove?.invoke(fromPosition, toPosition)
    }

    override fun onItemDissmiss(position: Int) {
        //移除数据
        datas.removeAt(position)
        notifyItemRemoved(position)
        onItemDissmiss?.invoke(position)
    }

    init {
        this.layoutId = layoutId
        this.isStandard = isStandard
        datas = dataList
    }

    interface OnBind<in T> {
        fun onBind(itemRootView: View, position: Int, data: T)
    }

    interface Presenter
}
