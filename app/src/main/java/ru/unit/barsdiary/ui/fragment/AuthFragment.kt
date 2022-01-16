package ru.unit.barsdiary.ui.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.observeFreshly
import dagger.hilt.android.AndroidEntryPoint
import ru.unit.barsdiary.R
import ru.unit.barsdiary.databinding.FragmentAuthBinding
import ru.unit.barsdiary.lib.livedata.EventLiveData
import ru.unit.barsdiary.ui.fragment.dialog.ServerListDialogFragment
import ru.unit.barsdiary.ui.viewmodel.AuthViewModel


// todo: review logic
@AndroidEntryPoint
class AuthFragment : BaseFragment(R.layout.fragment_auth) {

    companion object {
        private const val SERVER_LIST_DIALOG_TAG = "serverListDialog"
    }

    private lateinit var serverListDialog: ServerListDialogFragment

    private val model: AuthViewModel by activityViewModels()
    private lateinit var binding: FragmentAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        serverListDialog = parentFragmentManager
            .findFragmentByTag(SERVER_LIST_DIALOG_TAG) as? ServerListDialogFragment ?: ServerListDialogFragment()

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fastBootEnabled = activity?.intent?.getBooleanExtra("DISABLE_FAST_BOOT", false) == false

        binding = FragmentAuthBinding.bind(view)
        with(binding) {
            lifecycleOwner = this@AuthFragment
            model = this@AuthFragment.model
        }

        binding.scrollView.overScrollMode = View.OVER_SCROLL_NEVER

        if (serverListDialog.isAdded) {
            serverListDialog.dismiss()
        }

        // to login or auth

        if (model.fastAuth() && fastBootEnabled) {
            activity?.supportFragmentManager?.commit {
                setCustomAnimations(R.anim.fade_in_short, R.anim.fade_out_short, R.anim.fade_in_short, R.anim.fade_out_short)
                replace(R.id.fragmentContainerView, NavigationFragment())
            }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                if (model.isAuthorized()) {
                    binding.motionLayout.setTransition(R.id.transitionStartToEnd)
                    model.auth()
                } else {
                    binding.motionLayout.setTransition(R.id.transitionStartToMiddle)
                }
                binding.motionLayout.transitionToEnd()
            }, 2000)
        }

        // onclick: send server, login, password
        binding.buttonSignIn.setOnClickListener {
            binding.motionLayout.setTransition(R.id.transitionMiddleToEnd)
            binding.motionLayout.transitionToEnd()

            binding.progressBarLine.progress = 0
            repoAuth()
        }

        // onclick: refresh
        binding.refreshButton.setOnClickListener {
            model.refresh()
        }

        binding.visibilityButtonView.setOnClickListener {
            model.passwordVisibility()
        }

        model.passwordVisibilityLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.visibilityButtonView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_round_visibility_off_24))
                binding.editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.visibilityButtonView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_round_visibility_24))
                binding.editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        // if list of servers got, then stop refreshing and disable focusable edittext
        model.serverListLiveData.observe(viewLifecycleOwner) { _ ->
            binding.editTextServer.isFocusable = false

            // onclick: if edittext is empty, then show ServerListDialog
            binding.editTextServer.setOnClickListener {
                serverListDialog.setOnClickListener {
                    if (it.isEmpty()) {
                        binding.editTextServer.isFocusableInTouchMode = true
                        binding.editTextServer.setText(getString(R.string.https))
                        binding.editTextServer.setOnClickListener(null)
                    } else {
                        binding.editTextServer.setText(it)
                    }
                }

                if (!serverListDialog.isAdded) {
                    serverListDialog.show(parentFragmentManager, SERVER_LIST_DIALOG_TAG)
                }
            }
        }

        // if got error, then show infoDialog and stop refreshing
        model.refreshExceptionLiveData.observeFreshly(viewLifecycleOwner) {
            binding.refreshButton.state(it != null)

            if (it != null) {
                mainModel.handleException(it)
            }
        }

        // if got error, then show infoDialog and stop refreshing
        model.authExceptionLiveData.observeFreshly(viewLifecycleOwner, {
            it ?: return@observeFreshly

            mainModel.handleException(it)

            binding.motionLayout.setTransition(R.id.transitionMiddleToEnd)
            binding.motionLayout.transitionToStart()
        })

        // slowly update progress bar
        model.progressLiveData.observeFreshly(viewLifecycleOwner, {
            val animator = ObjectAnimator.ofInt(binding.progressBarLine, "progress", it)
            with(animator) {
                duration = 1500
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener { valueAnimator ->
                    if (valueAnimator.animatedValue == 100) {
                        activity?.supportFragmentManager?.commit {
                            setCustomAnimations(R.anim.fade_in_short, R.anim.fade_out_short, R.anim.fade_in_short, R.anim.fade_out_short)
                            replace(R.id.fragmentContainerView, NavigationFragment())
                        }
                    }
                }
                start()
            }
        })

        model.eventLiveData.observe(viewLifecycleOwner) {
            it ?: return@observe

            when (it) {
                EventLiveData.Event.LOADING -> binding.refreshButton.refreshStart()
                EventLiveData.Event.LOADED -> binding.refreshButton.refreshStop()
            }
        }

        model.refresh()
    }

    private fun repoAuth() {
        model.progressLiveData.value = 0
        model.auth()
    }
}