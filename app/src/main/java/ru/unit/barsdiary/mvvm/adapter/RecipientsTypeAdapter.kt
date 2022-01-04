package ru.unit.barsdiary.mvvm.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.unit.barsdiary.mvvm.fragment.RecipientsSearchFragment

class RecipientsTypeAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val story = arrayOf<Fragment>(
        RecipientsSearchFragment().apply {
            arguments = Bundle().apply {
                putInt("type", 0)
            }
        },
        RecipientsSearchFragment().apply {
            arguments = Bundle().apply {
                putInt("type", 1)
            }
        },
        RecipientsSearchFragment().apply {
            arguments = Bundle().apply {
                putInt("type", 2)
            }
        },
        RecipientsSearchFragment().apply {
            arguments = Bundle().apply {
                putInt("type", 3)
            }
        }
    )

    override fun getItemCount(): Int = story.size

    override fun createFragment(position: Int): Fragment {
        return story[position]
    }

    fun setSearchText(text: String) {
        story.forEach {
            if(it is RecipientsSearchFragment) {
                it.setSearch(text)
            }
        }
    }

    fun search() {
        story.forEach {
            if(it is RecipientsSearchFragment) {
                it.search()
            }
        }
    }
}