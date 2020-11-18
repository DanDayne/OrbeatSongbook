package com.dandayne.orbeatsongbook.ui.files

import androidx.recyclerview.widget.RecyclerView

interface FileDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}