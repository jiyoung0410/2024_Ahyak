package com.example.ahyak.RecordSymptoms

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AutoFitGridLayoutManager(context: Context, private var columnWidth: Int) :
    GridLayoutManager(context, 1) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (columnWidth > 0) {
            val totalWidth = width - paddingRight - paddingLeft
            val spanCount = totalWidth / columnWidth
            setSpanCount(if (spanCount > 0) spanCount else 1)
        }
        super.onLayoutChildren(recycler, state)
    }
}
