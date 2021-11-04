package ru.unit.barsdiary.other

import android.content.Context
import android.util.TypedValue

fun Context.questionAttribute(attr: Int): TypedValue {
    val typedValue = TypedValue()
    val theme = theme
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue
}