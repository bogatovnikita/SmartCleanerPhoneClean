package com.entertainment.event.ssearch.ar155.adapters
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class HintDecoration(
    private val offset: Float = 12f,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = offset.dpToPx()
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            if (!parent.isAnimating) {
                val view = parent.getChildAt(i)
                view.alpha = (1.0 - (i + 0.1) * 0.3).toFloat()
            } else break
        }
    }
}


fun Float.dpToPx(): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    Resources.getSystem().displayMetrics
).roundToInt()