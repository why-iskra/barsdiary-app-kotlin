package ru.unit.barsdiary.mvvm.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.unit.barsdiary.mvvm.fragment.*

class NavigationAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val story = arrayOf<Fragment?>(null, null, null, null)

    override fun getItemCount(): Int = story.size

    override fun createFragment(position: Int): Fragment {
        var fragment = story[position]
        if (fragment == null) {
            fragment = when (position) {
                FragmentId.Diary.id -> DiaryFragment()
                FragmentId.Statistics.id -> StatisticsFragment()
                FragmentId.Global.id -> GlobalFragment()
                FragmentId.Account.id -> AccountFragment()
                else -> PlugFragment()
            }

            story[position] = fragment
        }
        return fragment
    }

    enum class FragmentId(val id: Int) {
        Diary(0),
        Statistics(1),
        Global(2),
        Account(3)
    }
}