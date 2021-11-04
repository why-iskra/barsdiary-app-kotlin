package ru.unit.barsdiary.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import ru.unit.barsdiary.R
import ru.unit.barsdiary.other.dp

@SuppressLint("ResourceAsColor")
class ShowHideButtonView : LinearLayoutCompat {

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context) : super(context)

    private var show = false

    private val textView = TextView(context).apply {
        setTextAppearance(R.style.TextAppearance_Barsdiary_Small)
        typeface = ResourcesCompat.getFont(context, R.font.nunito_bold)
    }

    private val imageView = ImageView(context)

    init {
        setBackgroundResource(R.drawable.ripple)
        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        setPaddingRelative(
            4.dp(context),
            2.dp(context),
            10.dp(context),
            2.dp(context)
        )

        addView(imageView)
        addView(textView)

        isClickable = true
        update()
    }

    fun setOnShowHideListener(listener: (value: Boolean) -> Unit) {
        setOnClickListener {
            show = !show
            update()

            listener.invoke(show)
        }
    }

    private fun update() {
        @DrawableRes val imageId: Int
        @StringRes val textId: Int

        if (show) {
            imageId = R.drawable.ic_round_expand_more_24
            textId = R.string.show
        } else {
            imageId = R.drawable.ic_round_expand_less_24
            textId = R.string.hide
        }

        imageView.setImageDrawable(ContextCompat.getDrawable(context, imageId))
        textView.setText(textId)
    }

    fun setShow(value: Boolean) {
        show = value
        update()
    }

    fun getShow() = show
}