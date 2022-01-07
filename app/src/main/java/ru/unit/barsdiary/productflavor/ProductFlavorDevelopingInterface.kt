package ru.unit.barsdiary.productflavor

import android.app.Activity
import android.content.Context
import android.view.View

interface ProductFlavorDevelopingInterface {

    fun init(context: Context)

    fun removeStatusBar(activity: Activity, view: View)

}