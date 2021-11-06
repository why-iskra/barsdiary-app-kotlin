package ru.unit.barsdiary.mvvm.fragment

import android.os.Bundle
import android.view.View
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentChildBinding

class ChildFragment : BaseFragment(R.layout.fragment_child) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentChildBinding.bind(view)

        arguments?.let {
//            binding.textViewId.text = it.getInt("id").toString()
            binding.textViewName.text = it.getString("name")
            binding.textViewSchool.text = it.getString("school")
        }
    }

    fun config(id: Int, name: String?, school: String?) {
        arguments = Bundle().apply {
            putInt("id", id)
            putString("name", name)
            putString("school", school)
        }
    }

}