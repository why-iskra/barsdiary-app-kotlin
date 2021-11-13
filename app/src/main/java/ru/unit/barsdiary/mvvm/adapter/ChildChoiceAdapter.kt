package ru.unit.barsdiary.mvvm.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.unit.barsdiary.domain.auth.pojo.ChildPojo
import ru.unit.barsdiary.mvvm.fragment.ChildFragment

class ChildChoiceAdapter(
    fragment: Fragment,
    private val children: List<ChildPojo>,
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = children.size

    override fun createFragment(position: Int): Fragment {
        val pupil = children[position]
        val fragment = ChildFragment()

        fragment.arguments = ChildFragment.config(pupil.id, pupil.name, pupil.school)

        return fragment
    }
}