import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.text.TextPaint
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.montdeska.android_hacker_news.R

abstract class SwipeToDelete(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val _context = context
    private val deleteString = "Delete"
    private val background = ColorDrawable()

    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder.adapterPosition == 10) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                canvas,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(
                canvas,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
            return
        }

        background.color = ContextCompat.getColor(_context, R.color.delete)
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(canvas)
        val intrinsicWidth = deleteString.length
        val paintText = TextPaint().apply {
            color = Color.WHITE
            strokeWidth = dpToPx(1)
            textSize = dpToPx(16)
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
        }

        val bounds = calculateOriginTextBound(paintText)
        val deleteIconTop = itemView.top + (itemHeight - bounds.top) / 2
        val deleteIconMargin = (itemHeight - bounds.top) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth

        canvas.drawText(deleteString, deleteIconLeft.toFloat(), deleteIconTop.toFloat(), paintText)
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun calculateOriginTextBound(paintText: TextPaint): Rect {
        val textBound = Rect()
        paintText.getTextBounds(deleteString, 0, deleteString.length, textBound)
        return textBound
    }

    private fun dpToPx(value: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(), _context.resources.displayMetrics
        )
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

}