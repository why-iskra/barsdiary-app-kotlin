package ru.unit.barsdiary.ui.fragment

import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ru.unit.barsdiary.R

open class NavigationGraphFragment(@LayoutRes res: Int) : BaseFragment(res) {

    fun getNavController(): NavController? {
        runCatching {
            return view?.findViewById<FragmentContainerView>(R.id.fragmentContainerView)?.getFragment<NavHostFragment>()?.navController
        }

        return null
    }

}