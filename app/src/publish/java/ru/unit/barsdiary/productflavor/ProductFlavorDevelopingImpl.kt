package ru.unit.barsdiary.productflavor

import android.app.Activity
import android.content.Context
import android.view.View
import javax.inject.Inject

class ProductFlavorDevelopingImpl @Inject constructor() : ProductFlavorDevelopingInterface {
    override fun init(context: Context) { /* nothing */ }

    override fun removeStatusBar(activity: Activity, view: View) { /* nothing */ }

}