package ru.unit.barsdiary.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter.FragmentTransactionCallback.OnPostEventListener
import ru.unit.barsdiary.ui.fragment.*


class NavigationAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    init {
        registerFragmentTransactionCallback(object : FragmentTransactionCallback() {
            override fun onFragmentMaxLifecyclePreUpdated(
                fragment: Fragment,
                maxLifecycleState: Lifecycle.State
            ) = if (maxLifecycleState == Lifecycle.State.RESUMED) {
                OnPostEventListener {
                    fragment.parentFragmentManager.commitNow {
                        setPrimaryNavigationFragment(fragment)
                    }
                }
            } else {
                super.onFragmentMaxLifecyclePreUpdated(fragment, maxLifecycleState)
            }
        })
    }

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FragmentId.Diary.id -> NavigationDiaryFragment()
            FragmentId.Statistics.id -> NavigationStatisticsFragment()
            FragmentId.Global.id -> NavigationGlobalFragment()
            FragmentId.Account.id -> NavigationAccountFragment()
            else -> PlugFragment()
        }
    }

    enum class FragmentId(val id: Int) {
        Diary(0),
        Statistics(1),
        Global(2),
        Account(3)
    }
}