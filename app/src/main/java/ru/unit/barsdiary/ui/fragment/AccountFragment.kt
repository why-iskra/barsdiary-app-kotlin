package ru.unit.barsdiary.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observeFreshly
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.flow.collectLatest
import ru.unit.barsdiary.BuildConfig
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentAccountBinding
import ru.unit.barsdiary.other.function.inflateFactory
import ru.unit.barsdiary.ui.activity.StartActivity
import ru.unit.barsdiary.ui.fragment.dialog.ActionDialogFragment
import ru.unit.barsdiary.ui.viewmodel.AccountViewModel
import ru.unit.barsdiary.other.livedata.EventLiveData


@AndroidEntryPoint
class AccountFragment : BaseFragment(R.layout.fragment_account) {

    companion object {
        private const val ACTION_DIALOG_TAG = "actionDialog"
    }

    private val model: AccountViewModel by activityViewModels()
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
                actionDialog.arguments = ActionDialogFragment.config(getString(R.string.logout), getString(R.string.logout_text))
                actionDialog.show(parentFragmentManager, ACTION_DIALOG_TAG)
            }
        }

        binding.textViewAboutApp.visibility = if (BuildConfig.DEBUG) View.VISIBLE else View.GONE

        binding.textViewDeveloper.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_developerFragment)
        }

        binding.textViewChildChoice.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_childChoiceFragment)
        }

        binding.textViewInformation.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_informationFragment)
        }

        binding.textViewFinalMarks.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_finalMarksFragment)
        }

        binding.textViewAboutApp.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_aboutApplicationFragment)
        }

        binding.textViewSettings.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_settingsFragment)
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
                EventLiveData.Event.LOADED -> {
                    binding.refreshButton.refreshStop()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.nameLayout.visibility = View.VISIBLE
                }
            }
        }

        model.personAvatarLiveData.observe(viewLifecycleOwner) {
            val context = context
            if (it != null && context != null) {
                Picasso.get()
                    .load(it)
                    .transform(CropCircleTransformation())
                    .into(binding.avatarView)
            }
        }

        model.exceptionLiveData.observeFreshly(viewLifecycleOwner) {
            binding.refreshButton.state(it != null)

            if (it != null) {
                mainModel.handleException(it)
            }
        }

        model.refresh()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            model.settingsDataStore.flow.collectLatest {
                binding.textViewDeveloper.visibility = if (it.developerMode == true) View.VISIBLE else View.GONE
            }
        }
    }
}