package ru.unit.barsdiary.mvvm.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.BuildConfig
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentAboutApplicationBinding

@AndroidEntryPoint
class AboutApplicationFragment : BaseFragment(R.layout.fragment_about_application) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAboutApplicationBinding.bind(view)

        binding.textViewVersion.text = "${BuildConfig.VERSION_NAME} ${BuildConfig.BUILD_TYPE}"

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        binding.textViewPlayMarket.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=ru.unit.barsdiary")))
        }
    }

}