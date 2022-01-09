package ru.unit.barsdiary.lib.function

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.configure() {
    val child = getChildAt(0) as? RecyclerView
    if (child != null) {
        child.overScrollMode = View.OVER_SCROLL_NEVER
        child.isHorizontalFadingEdgeEnabled = true
    }

    overScrollMode = View.OVER_SCROLL_NEVER
    isHorizontalFadingEdgeEnabled = true
}