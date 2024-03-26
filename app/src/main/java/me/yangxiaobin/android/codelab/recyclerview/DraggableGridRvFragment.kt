package me.yangxiaobin.android.codelab.recyclerview

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import me.yangxiaobin.android.codelab.R
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.getActionString
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.setOnItemClickListener
import me.yangxiaobin.android.kotlin.codelab.ext.uicontroller.showFragmentToast
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.getScreenLocation
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.kotlin.codelab.recyclerview.SimpleVH
import me.yangxiaobin.android.kotlin.codelab.recyclerview.SimpleRvAdapter
import me.yangxiaobin.logger.core.LogFacade

class DraggableGridRvFragment : AbsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "DraggableGridRvFragment"

    override val layoutResId: Int = R.layout.fragment_recyclerview

    private  val rvFragment by lazy { requireView().findViewById<RecyclerView>(R.id.rv_fragment) }

    @SuppressLint("ClickableViewAccessibility")
    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        val mockList = mutableListOf<Int>()
        repeat(30) { mockList += it }


        rvFragment.layoutManager = GridLayoutManager(requireContext(), 4)
        rvFragment.adapter = SimpleRvAdapter(
            mockList,
            android.R.layout.simple_list_item_1
        ) { (vh: SimpleVH, _, pos, _) ->

            vh.requireView<TextView>(android.R.id.text1).run {
                gravity = Gravity.CENTER
                text = mockList[pos].toString()
            }


            vh.itemView.setOnTouchListener { _: View, motionEvent: MotionEvent ->
                logD("grid item onTouch: ${motionEvent.getActionString}")
                true
            }

        }

        rvFragment.setOnItemClickListener(
            onLongClick = {
                logD("Grid onLongClick pos :${it.second}")
                showFragmentToast("LongClick")
                false
            },
            onClick = {
                logD("Grid onClick pos :${it.second}")
                showFragmentToast("Click")
            }
        )

        setupItemTouchHelper()
        setupItemDecoration()
    }

    private fun setupItemTouchHelper() {
        val helperCallback = @SuppressLint("SetTextI18n")
        object : LogAwareItemTouchHelperCallback(logD) {

            private val enable = makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, 0)
            private val disable = makeMovementFlags(0, 0)

            private val fakeView by lazy {
                TextView(requireContext())
                    .apply {
                        val lp = ViewGroup.MarginLayoutParams(200, 100)
                        this.layoutParams = lp
                        this.text = "FAKE_VIEW"
                        this.updatePadding(20, 20, 20, 20)
                        this.setBackgroundColor(Color.YELLOW)
                        this.setTextColor(Color.BLACK)
                    }
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int = enable

            override fun onMove(
                rv: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val oldPos = viewHolder.bindingAdapterPosition
                val newPos = target.bindingAdapterPosition

                val canMove = if (oldPos > 0) {
                    rv.adapter?.notifyItemMoved(oldPos, newPos)
                    true
                } else {
                    false
                }

                logD("onMove, vh:$viewHolder, targetVh:$target, res:$canMove.")

                return canMove
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                fakeView.isVisible = true
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                fakeView.isGone = true
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                val (screenX, screenY) = viewHolder.itemView.getScreenLocation

                if (!fakeView.isAttachedToWindow) {
                    (requireActivity().window.decorView as ViewGroup).addView(fakeView)
                }

                fakeView.translationX = screenX.toFloat()
                fakeView.translationY = screenY.toFloat()

            }
        }

        ItemTouchHelper(helperCallback).attachToRecyclerView(rvFragment)

    }

    private fun setupItemDecoration(){
        rvFragment.addItemDecoration(GridPaddingItemDecoration(4,20,false,logD))
    }
}
