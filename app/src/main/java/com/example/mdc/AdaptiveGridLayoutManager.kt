package com.example.mdc

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler


class AdaptiveGridLayoutManager(context: Context?, columnWidth: Int) :
    GridLayoutManager(context, 1) {
    private var columnWidth = 0
    private var columnWidthChanged = true

    init {
        setColumnWidth(columnWidth)
    }

    private fun setColumnWidth(newColumnWidth: Int) {
        val columnWidth = columnWidth
        if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
            this.columnWidth = newColumnWidth
            columnWidthChanged = true
        }
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        val columnWidth = columnWidth
        if (columnWidthChanged && columnWidth > 0) {
            val totalSpace: Int = if (orientation == VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }
            val spanCount = Math.max(1, totalSpace / columnWidth)
            setSpanCount(spanCount)
            columnWidthChanged = false
        }
        super.onLayoutChildren(recycler, state)
    }
}