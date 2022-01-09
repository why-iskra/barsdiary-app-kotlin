package ru.unit.barsdiary.lib.function

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import kotlin.properties.ReadOnlyProperty

@Suppress("unused")
inline fun <reified T> Activity.argumentDelegate(): ReadOnlyProperty<Activity, T> {
    return argumentDelegate { it.intent?.extras }
}

@Suppress("unused")
inline fun <reified T> Fragment.argumentDelegate(): ReadOnlyProperty<Fragment, T> {
    return argumentDelegate { it.requireArguments() }
}

@Suppress("unused")
inline fun <reified T> ViewModel.argumentDelegate(args: Bundle): ReadOnlyProperty<ViewModel, T> {
    return argumentDelegate { args }
}

inline fun <F, reified T> argumentDelegate(crossinline provideArguments: (F) -> Bundle?) = ReadOnlyProperty<F, T> { thisRef, property ->
    provideArguments(thisRef)?.get(property.name) as T
}