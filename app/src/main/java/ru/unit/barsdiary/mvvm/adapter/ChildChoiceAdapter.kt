package ru.unit.barsdiary.mvvm.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.unit.barsdiary.domain.auth.pojo.PupilPojo
import ru.unit.barsdiary.mvvm.fragment.ChildFragment

class ChildChoiceAdapter(
    fragment: Fragment,
    private val pupils: List<PupilPojo>,
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = pupils.size

    override fun createFragment(position: Int): Fragment {
        val pupil = pupils[position]
        val fragment = ChildFragment()

        fragment.config(pupil.id, pupil.name, pupil.school)

        return fragment
    }
}