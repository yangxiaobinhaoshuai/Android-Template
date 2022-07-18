package me.yangxiaobin.android.codelab.recyclerview

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


abstract class LogAwareItemTouchHelperCallback(
    private val log: (message: String) -> Unit,
) : ItemTouchHelper.Callback() {


    private fun logD(message: String) = log("ItemTouchCallback: $message")

    private fun Int.actionStateToString() = when (this) {
        ItemTouchHelper.ACTION_STATE_DRAG -> "DRAG"
        ItemTouchHelper.ACTION_STATE_IDLE -> "IDLE"
        ItemTouchHelper.ACTION_STATE_SWIPE -> "SWIPE"
        else -> throw  IllegalArgumentException()
    }

    override fun isLongPressDragEnabled(): Boolean {
        return super.isLongPressDragEnabled().also { logD("isLongPressDragEnabled:$it.") }
    }

    override fun getMoveThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return super.getMoveThreshold(viewHolder)
            .also { logD("getMoveThreshold, vh:${viewHolder}, res:$it.") }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        logD("onSelectedChanged, vh:${viewHolder}, actionState:${actionState.actionStateToString()}.")
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        logD("clearView, vh:${viewHolder}.")
    }

    override fun chooseDropTarget(
        selected: RecyclerView.ViewHolder,
        dropTargets: MutableList<RecyclerView.ViewHolder>,
        curX: Int,
        curY: Int
    ): RecyclerView.ViewHolder? {
        return super.chooseDropTarget(selected, dropTargets, curX, curY).also {
            logD(
                """
            chooseDropTarget:
            selected:$selected
            dropTargets:$dropTargets
            curX:$curX
            curY:$curY
            res :$it
        """.trimIndent()
            )
        }
    }

    override fun canDropOver(
        recyclerView: RecyclerView,
        current: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return super.canDropOver(recyclerView, current, target)
            .also { logD("canDropOver, currentVh:$current, targetVh:$target, res:$it.") }
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
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        logD(
            """
            onChildDraw
            vh:$viewHolder
            dX:$dX
            dY:$dY
            actionState:${actionState.actionStateToString()}
            isCurrentlyActive:$isCurrentlyActive
        """.trimIndent()
        )
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        logD(
            """
            onChildDrawOver
            vh:$viewHolder
            dX:$dX
            dY:$dY
            actionState:${actionState.actionStateToString()}
            isCurrentlyActive:$isCurrentlyActive
        """.trimIndent()
        )
    }

    override fun onMoved(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        fromPos: Int,
        target: RecyclerView.ViewHolder,
        toPos: Int,
        x: Int,
        y: Int
    ) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)

        logD(
            """
            onMoved
            vh:$viewHolder
            fromPos:$fromPos
            targetVh:$target
            toPos:$toPos
            x:$x
            y:$y
        """.trimIndent()
        )
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        logD("onSwiped, vh:${viewHolder}, direction:$direction.")
    }

}
