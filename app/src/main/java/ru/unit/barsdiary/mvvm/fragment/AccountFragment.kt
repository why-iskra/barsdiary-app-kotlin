package ru.unit.barsdiary.mvvm.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.BuildConfig
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentAccountBinding
import ru.unit.barsdiary.mvvm.activity.StartActivity
import ru.unit.barsdiary.mvvm.fragment.dialog.ActionDialogFragment
import ru.unit.barsdiary.mvvm.viewmodel.PersonViewModel
import ru.unit.barsdiary.other.inflateFactory
import ru.unit.barsdiary.other.livedata.EventLiveData

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    companion object {
        private const val ACTION_DIALOG_TAG = "actionDialog"
    }

    private val model: PersonViewModel by activityViewModels()
    private lateinit var binding: FragmentAccountBinding

    private lateinit var actionDialog: ActionDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        actionDialog = parentFragmentManager
            .findFragmentByTag(ACTION_DIALOG_TAG) as? ActionDialogFragment ?: ActionDialogFragment()

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAccountBinding.bind(view)
        with(binding) {
            lifecycleOwner = this@AccountFragment
            model = this@AccountFragment.model
        }

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        binding.textSwitcherPersonName.inflateFactory(layoutInflater, R.layout.text_switcher_person_person_name)
        binding.textSwitcherPersonParentName.inflateFactory(layoutInflater, R.layout.text_switcher_person_person_parent_name)

        binding.refreshButton.setOnClickListener {
            model.reset()
            model.refresh()
        }

        binding.textViewLogout.setOnClickListener {
            if (!actionDialog.isAdded) {
                actionDialog.send(getString(R.string.logout), getString(R.string.logout_text))
                actionDialog.show(parentFragmentManager, ACTION_DIALOG_TAG)
            }
        }

        if (BuildConfig.DEBUG) {
            binding.textViewDeveloper.visibility = View.VISIBLE
            binding.textViewSettings.visibility = View.VISIBLE
        } else {
            binding.textViewDeveloper.visibility = View.GONE
            binding.textViewSettings.visibility = View.GONE
        }

        binding.textViewDeveloper.setOnClickListener {
            findNavController().navigate(R.id.action_navigationFragment_to_developerFragment)
        }

        binding.textViewChildChoice.setOnClickListener {
            findNavController().navigate(R.id.action_navigationFragment_to_childChoiceFragment)
        }

        binding.textViewProfile.setOnClickListener {
            findNavController().navigate(R.id.action_navigationFragment_to_profileFragment)
        }

        binding.textViewFinalMarks.setOnClickListener {
            findNavController().navigate(R.id.action_navigationFragment_to_finalMarksFragment)
        }

        actionDialog.setActionListener {
            model.logout()
            activity?.finishAffinity()
            startActivity(Intent(context, StartActivity::class.java))
        }

        model.isParentLiveData.observe(viewLifecycleOwner) {
            binding.textSwitcherPersonParentName.visibility = if (it) View.VISIBLE else View.GONE
            binding.textViewChildChoice.visibility = if (it) View.VISIBLE else View.GONE
        }

        model.eventLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe

            when (it) {
                EventLiveData.Event.LOADING -> binding.refreshButton.refreshStart()
                EventLiveData.Event.LOADED -> binding.refreshButton.refreshStop()
            }
        }

        model.refresh()
    }
}