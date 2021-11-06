package ru.unit.barsdiary.mvvm.fragment

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.unit.barsdiary.mvvm.viewmodel.MainViewModel

open class BaseFragment(@LayoutRes val res: Int) : Fragment(res) {

    val mainModel: MainViewModel by activityViewModels()

}