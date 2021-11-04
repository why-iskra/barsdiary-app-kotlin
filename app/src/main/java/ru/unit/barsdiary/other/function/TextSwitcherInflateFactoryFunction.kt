package ru.unit.barsdiary.other

import android.view.LayoutInflater
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.annotation.LayoutRes

fun TextSwitcher.inflateFactory(inflater: LayoutInflater, @LayoutRes resource: Int, configurator: (TextView) -> Unit = {}) {
    setFactory {
        val textView: TextView = inflater.inflate(resource, null) as TextView
        configurator(textView)
        textView
    }
}