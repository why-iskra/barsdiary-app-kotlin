package ru.unit.barsdiary.other

import android.content.Context
import android.util.TypedValue

fun Int.dp(context: Context): Int = if (this == 0) 0 else
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()

fun Float.dp(context: Context): Float = if (this == 0F) 0F else
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)

fun Int.sp(context: Context): Int = if (this == 0) 0 else
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), context.resources.displayMetrics).toInt()

fun Float.sp(context: Context): Float = if (this == 0F) 0F else
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics)