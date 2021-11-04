package ru.unit.barsdiary.components

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import ru.unit.barsdiary.R
import ru.unit.barsdiary.other.endAnimatorListener

class RefreshButtonView : FrameLayout {

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) : super(context,
        attrs,
        defStyleAttr,
        defStyleRes)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context) : super(context)

    private val imageView: ImageView = ImageButtonView(context).apply {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_round_refresh_24))
        visibility = View.VISIBLE
    }

    private val progressBar: ProgressBar = ProgressBar(context, null, android.R.attr.progressBarStyleSmall).apply {
        visibility = View.INVISIBLE
    }

    private var refresh = false

    init {
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        }

        addView(imageView, params)
        addView(progressBar, params)

        minimumWidth = 4
        minimumHeight = 4
    }

    override fun setOnClickListener(l: OnClickListener?) {
        setOnClickRefreshListener(false, l)
    }

    fun setOnClickRefreshListener(automatic: Boolean = true, l: OnClickListener?) {
        if (l != null) {
            imageView.setOnClickListener {
                if (!refresh) {
                    if (automatic) {
                        refreshStart()
                    }

                    l.onClick(it)
                }
            }
        }
    }

    fun refreshStart() {
        if (refresh) return

        refresh = true
        fadeIn.run { cancel(); reset() }
        fadeOut.run { cancel(); reset() }

        fadeOut.setAnimationListener(endAnimatorListener {
            imageView.visibility = INVISIBLE
            progressBar.startAnimation(fadeIn)
            progressBar.visibility = VISIBLE
        })
        imageView.startAnimation(fadeOut)
    }

    fun refreshStop() {
        if (!refresh) return

        refresh = false

        fadeIn.run { cancel(); reset() }
        fadeOut.run { cancel(); reset() }

        fadeOut.setAnimationListener(endAnimatorListener {
            progressBar.visibility = INVISIBLE
            imageView.startAnimation(fadeIn)
            imageView.visibility = VISIBLE
        })
        progressBar.startAnimation(fadeOut)
    }

    private val fadeIn = AlphaAnimation(0f, 1f).apply {
        interpolator = DecelerateInterpolator()
        duration = 500
    }

    private val fadeOut = AlphaAnimation(1f, 0f).apply {
        interpolator = DecelerateInterpolator()
        duration = 500
    }
}