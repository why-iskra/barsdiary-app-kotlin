package ru.unit.barsdiary.mvvm.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.unit.barsdiary.mvvm.fragment.MailInBoxFragment
import ru.unit.barsdiary.mvvm.fragment.MailOutBoxFragment
import ru.unit.barsdiary.mvvm.fragment.PlugFragment

class MailAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val story = arrayOf<Fragment?>(null, null)

    override fun getItemCount(): Int = story.size

    override fun createFragment(position: Int): Fragment {
        var fragment = story[position]
        if (fragment == null) {
            fragment = when (position) {
                FragmentId.IN_BOX.id -> MailInBoxFragment()
                FragmentId.OUT_BOX.id -> MailOutBoxFragment()
                else -> PlugFragment()
            }

            story[position] = fragment
        }
        return fragment
    }

    enum class FragmentId(val id: Int) {
        IN_BOX(0),
        OUT_BOX(1)
    }
}