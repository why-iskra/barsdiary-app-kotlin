package ru.unit.barsdiary.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.annotation.AttrRes
import ru.unit.barsdiary.R

class TwoWayScrollView : ScrollView {

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context) : super(context)

    private var horizontalScrollView: HorizontalScrollView? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        horizontalScrollView = findViewById(R.id.horizontalScrollView)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var result = super.onTouchEvent(event)
        result = result or (horizontalScrollView?.dispatchTouchEvent(event) ?: false)
        return result
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        super.onInterceptTouchEvent(event)
        horizontalScrollView?.onInterceptTouchEvent(event)
        return true
    }
}