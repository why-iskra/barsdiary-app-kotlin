package ru.unit.barsdiary.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.unit.barsdiary.ui.fragment.LogsFragment
import ru.unit.barsdiary.ui.fragment.PanelFragment
import ru.unit.barsdiary.ui.fragment.PlugFragment

class DeveloperAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FragmentId.LOG.id -> LogsFragment()
            FragmentId.PANEL.id -> PanelFragment()
            else -> PlugFragment()
        }
    }

    enum class FragmentId(val id: Int) {
        LOG(0),
        PANEL(1)
    }
}