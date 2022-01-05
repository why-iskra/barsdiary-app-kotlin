package ru.unit.barsdiary.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.unit.barsdiary.domain.auth.pojo.ChildPojo
import ru.unit.barsdiary.ui.fragment.ChildFragment

class ChildChoiceAdapter(
    fragmentActivity: FragmentActivity,
    private val children: List<ChildPojo>,
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = children.size

    override fun createFragment(position: Int): Fragment {
        val pupil = children[position]
        val fragment = ChildFragment()

        fragment.arguments = ChildFragment.config(pupil.id, pupil.name, pupil.school)

        return fragment
    }
}