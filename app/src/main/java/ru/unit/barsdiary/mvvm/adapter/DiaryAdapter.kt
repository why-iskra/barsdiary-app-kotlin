package ru.unit.barsdiary.mvvm.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.unit.barsdiary.mvvm.fragment.LessonsFragment
import ru.unit.barsdiary.mvvm.fragment.PlugFragment
import java.time.LocalDate

class DiaryAdapter(fragment: Fragment, private val date: LocalDate) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 1000

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return PlugFragment()
        }

        val fragment = LessonsFragment()
        fragment.config(date.plusDays(position - 500L).toEpochDay())
        return fragment
    }
}