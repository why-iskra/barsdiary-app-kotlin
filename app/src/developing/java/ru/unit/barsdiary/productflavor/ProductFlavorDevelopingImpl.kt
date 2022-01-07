package ru.unit.barsdiary.productflavor

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import javax.inject.Inject

class ProductFlavorDevelopingImpl @Inject constructor() : ProductFlavorDevelopingInterface {
    override fun init(context: Context) {
        Toast.makeText(context, "Use Developing Version", Toast.LENGTH_SHORT).show()
    }

    override fun removeStatusBar(activity: Activity, view: View) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        WindowInsetsControllerCompat(activity.window, view).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

}