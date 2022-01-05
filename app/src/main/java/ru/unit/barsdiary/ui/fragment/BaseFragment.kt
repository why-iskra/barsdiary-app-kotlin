package ru.unit.barsdiary.ui.fragment

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.unit.barsdiary.ui.viewmodel.MainViewModel

open class BaseFragment(@LayoutRes res: Int) : Fragment(res) {

    val mainModel: MainViewModel by activityViewModels()

}