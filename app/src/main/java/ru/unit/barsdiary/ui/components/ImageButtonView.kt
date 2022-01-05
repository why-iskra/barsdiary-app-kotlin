package ru.unit.barsdiary.ui.components

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatImageView
import ru.unit.barsdiary.R
import ru.unit.barsdiary.other.dp
import ru.unit.barsdiary.other.function.questionAttribute

class ImageButtonView : AppCompatImageView {

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context) : super(context)

    init {
        setBackgroundResource(context.questionAttribute(R.attr.selectableItemBackgroundBorderless).resourceId)

        (2.dp(context)).let {
            setPaddingRelative(it, it, it, it)
        }

        isClickable = true
        scaleType = ScaleType.CENTER_INSIDE
    }
}