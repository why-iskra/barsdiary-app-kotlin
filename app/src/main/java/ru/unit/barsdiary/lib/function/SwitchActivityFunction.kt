package ru.unit.barsdiary.lib.function

import android.app.Activity
import android.content.Context
import android.content.Intent
import ru.unit.barsdiary.R

fun <T> Activity.switchActivity(context: Context?, clazz: Class<T>, flags: Int? = null, configurator: (intent: Intent) -> Unit = {}) {
    if (context != null) {
        val intent = Intent(context, clazz)
        flags?.let {
            intent.addFlags(it)
        }

        configurator.invoke(intent)

        context.startActivity(intent)
        overridePendingTransition(R.anim.activity_in_anim, R.anim.activity_out_anim)
        finish()
    }
}