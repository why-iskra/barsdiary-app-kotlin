package ru.unit.barsdiary.mvvm.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.unit.barsdiary.mvvm.fragment.NavigationAccountFragment
import ru.unit.barsdiary.mvvm.fragment.NavigationDiaryFragment
import ru.unit.barsdiary.mvvm.fragment.NavigationGlobalFragment
import ru.unit.barsdiary.mvvm.fragment.NavigationStatisticsFragment


class NavigationAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FragmentId.Diary.id -> NavigationDiaryFragment()
            FragmentId.Statistics.id -> NavigationStatisticsFragment()
            FragmentId.Global.id -> NavigationGlobalFragment()
            else -> NavigationAccountFragment()
        }
    }

    enum class FragmentId(val id: Int) {
        Diary(0),
        Statistics(1),
        Global(2),
        Account(3)
    }
}