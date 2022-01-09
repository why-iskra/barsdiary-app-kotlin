package ru.unit.barsdiary.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import ru.unit.barsdiary.R
import ru.unit.barsdiary.lib.dp
import ru.unit.barsdiary.lib.function.questionAttribute

class CountView : AppCompatTextView {

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context) : super(context)

    private val normalColor = context.questionAttribute(R.attr.colorAccent).resourceId
    private val accentColor = R.color.amaranth

    var count: Int = 0
        set(value) {
            field = value
            if(count == 0) {
                visibility = View.GONE
            } else {
                visibility = View.VISIBLE
                text = if(count > 100) "100+" else count.toString()
            }
        }

    var accent: Boolean = false
        set(value) {
            field = value
            backgroundTintList = ContextCompat.getColorStateList(
                context,
                if(value) accentColor else normalColor
            )
        }

    init {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            setPadding(8.dp(context), 2.dp(context), 8.dp(context), 2.dp(context))
        }
        setTextAppearance(R.style.TextAppearance_Barsdiary_Small)
        setTextColor(ContextCompat.getColor(context, R.color.white))
        typeface = ResourcesCompat.getFont(context, R.font.nunito_bold)
        background = ContextCompat.getDrawable(context, R.drawable.background_count_view)

        count = 0
        accent = false
    }
}