package ru.unit.barsdiary.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.BuildConfig
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentPanelBinding
import ru.unit.barsdiary.ui.viewmodel.DeveloperViewModel

@AndroidEntryPoint
class PanelFragment : BaseFragment(R.layout.fragment_panel) {

    private val model: DeveloperViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPanelBinding.bind(view)

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        binding.fakeAuthButton.setOnClickListener { model.fakeAuth() }
        binding.clearAllButton.setOnClickListener { model.clearAll() }
        binding.crashButton.setOnClickListener { model.crash() }

        binding.chuckerSwitch.isChecked = model.settingsDataStore.enableChucker
        binding.chuckerSwitch.setOnCheckedChangeListener { _, isChecked ->
            model.settingsDataStore.enableChucker = isChecked
        }

        binding.webSessionIdButton.text = "Web Session Id: ${model.getSessionId()}"
        binding.webSessionIdButton.setOnClickListener {
            model.sessionIdToClipboard()
        }

        binding.crashButton.isVisible = BuildConfig.DEBUG
    }
}