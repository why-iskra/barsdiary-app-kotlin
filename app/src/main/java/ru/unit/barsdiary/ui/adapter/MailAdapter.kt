package ru.unit.barsdiary.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.unit.barsdiary.ui.fragment.MailBoxFragment
import ru.unit.barsdiary.ui.fragment.PlugFragment

class MailAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FragmentId.IN_BOX.id -> MailBoxFragment().apply {
                arguments = Bundle().apply {
                    putBoolean("inBox", true)
                }
            }
            FragmentId.OUT_BOX.id -> MailBoxFragment().apply {
                arguments = Bundle().apply {
                    putBoolean("inBox", false)
                }
            }
            else -> PlugFragment()
        }
    }

    enum class FragmentId(val id: Int) {
        IN_BOX(0),
        OUT_BOX(1)
    }
}