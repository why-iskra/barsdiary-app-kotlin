package ru.unit.barsdiary.mvvm.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.unit.barsdiary.mvvm.fragment.LogsFragment
import ru.unit.barsdiary.mvvm.fragment.PanelFragment
import ru.unit.barsdiary.mvvm.fragment.PlugFragment

class DeveloperAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val story = arrayOf<Fragment?>(null, null)

    override fun getItemCount(): Int = story.size

    override fun createFragment(position: Int): Fragment {
        var fragment = story[position]
        if (fragment == null) {
            fragment = when (position) {
                FragmentId.LOG.id -> LogsFragment()
                FragmentId.PANEL.id -> PanelFragment()
                else -> PlugFragment()
            }

            story[position] = fragment
        }
        return fragment
    }

    enum class FragmentId(val id: Int) {
        LOG(0),
        PANEL(1)
    }
}