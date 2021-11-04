package ru.unit.barsdiary.other.function

import android.app.Activity
import android.content.Context
import android.content.Intent
import ru.unit.barsdiary.R

fun <T> Activity.switchActivity(context: Context?, clazz: Class<T>) {
    if (context != null) {
        val intent = Intent(context, clazz)
        context.startActivity(intent)
        overridePendingTransition(R.anim.activity_in_anim, R.anim.activity_out_anim)
        finish()
    }
}