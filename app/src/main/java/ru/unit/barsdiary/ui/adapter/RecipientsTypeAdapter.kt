package ru.unit.barsdiary.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.unit.barsdiary.ui.fragment.RecipientsSearchFragment

class RecipientsTypeAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return RecipientsSearchFragment().apply {
            arguments = Bundle().apply {
                putInt("type", position)
            }
        }
    }
}