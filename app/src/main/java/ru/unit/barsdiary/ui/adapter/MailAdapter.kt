package ru.unit.barsdiary.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.unit.barsdiary.ui.fragment.MailInBoxFragment
import ru.unit.barsdiary.ui.fragment.MailOutBoxFragment
import ru.unit.barsdiary.ui.fragment.PlugFragment

class MailAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FragmentId.IN_BOX.id -> MailInBoxFragment()
            FragmentId.OUT_BOX.id -> MailOutBoxFragment()
            else -> PlugFragment()
        }
    }

    enum class FragmentId(val id: Int) {
        IN_BOX(0),
        OUT_BOX(1)
    }
}