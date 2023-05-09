package ru.kekulta.explr.shared.utils

import android.view.View
import android.widget.HorizontalScrollView

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun HorizontalScrollView.scrollToBottom() {
    val lastChild = getChildAt(childCount - 1)
    val left = lastChild.left + paddingLeft
    val delta = left - (scrollX + width)
    smoothScrollBy(-delta, 0)
}

fun HorizontalScrollView.hideScrollBar() {
    this.isVerticalScrollBarEnabled = false
    this.isHorizontalScrollBarEnabled = false
}