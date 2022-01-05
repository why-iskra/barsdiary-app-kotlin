package ru.unit.barsdiary.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.unit.barsdiary.ui.fragment.LessonsFragment
import ru.unit.barsdiary.ui.fragment.PlugFragment
import java.time.LocalDate

class DiaryAdapter(fragmentActivity: FragmentActivity, private val date: LocalDate) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 1000

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return PlugFragment()
        }

        val fragment = LessonsFragment()
        fragment.arguments = LessonsFragment.config(date.plusDays(position - 500L).toEpochDay())
        return fragment
    }
}