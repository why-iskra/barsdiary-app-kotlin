package ru.unit.barsdiary.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.BuildConfig
import ru.unit.barsdiary.R
import ru.unit.barsdiary.data.datastore.SettingsDataStore
import ru.unit.barsdiary.databinding.FragmentAboutApplicationBinding
import javax.inject.Inject

@AndroidEntryPoint
class AboutApplicationFragment : BaseFragment(R.layout.fragment_about_application) {

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    private var developerClicks = 0

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAboutApplicationBinding.bind(view)

        binding.textViewVersion.text = "${BuildConfig.VERSION_NAME} ${BuildConfig.BUILD_TYPE}"

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        binding.textViewVersion.setOnClickListener {
            developerClicks++

            if (developerClicks == 10) {
                Toast.makeText(context, "Developer mode enabled. Please restart the app.", Toast.LENGTH_SHORT).show()
                settingsDataStore.developerMode = true
            }
        }

        binding.textViewPlayMarket.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=ru.unit.barsdiary")))
        }
    }

}