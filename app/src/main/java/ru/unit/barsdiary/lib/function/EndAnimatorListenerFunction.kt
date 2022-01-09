package ru.unit.barsdiary.lib.function

import android.view.animation.Animation

inline fun endAnimatorListener(crossinline func: () -> Unit): Animation.AnimationListener = object : Animation.AnimationListener {
    override fun onAnimationStart(animation: Animation?) {

    }

    override fun onAnimationEnd(animation: Animation?) {
        func()
    }

    override fun onAnimationRepeat(animation: Animation?) {

    }
}