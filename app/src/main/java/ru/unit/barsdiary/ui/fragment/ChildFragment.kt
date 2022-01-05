package ru.unit.barsdiary.ui.fragment

import android.os.Bundle
import android.view.View
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentChildBinding
import ru.unit.barsdiary.other.function.argumentDelegate

class ChildFragment : BaseFragment(R.layout.fragment_child) {

    companion object {
        private const val CHILD_ID_KEY = "childId"
        private const val NAME_KEY = "name"
        private const val SCHOOL_KEY = "school"

        fun config(id: Int, name: String?, school: String?): Bundle {
            val bundle = Bundle()
            bundle.putInt(CHILD_ID_KEY, id)
            bundle.putString(NAME_KEY, name)
            bundle.putString(SCHOOL_KEY, school)

            return bundle
        }
    }

//    private val childId: Int by argumentDelegate()
    private val name: String? by argumentDelegate()
    private val school: String? by argumentDelegate()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentChildBinding.bind(view)

//            binding.textViewId.text = it.getInt("id").toString()
        binding.textViewName.text = name
        binding.textViewSchool.text = school
    }
}