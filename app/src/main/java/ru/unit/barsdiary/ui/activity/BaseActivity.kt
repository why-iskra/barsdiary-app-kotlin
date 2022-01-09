package ru.unit.barsdiary.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.unit.barsdiary.R
import ru.unit.barsdiary.data.datastore.SettingsDataStore
import ru.unit.barsdiary.lib.function.switchActivity
import ru.unit.barsdiary.productflavor.ProductFlavorDevelopingInterface
import ru.unit.barsdiary.sdk.exception.FinishRegistrationAccountException
import ru.unit.barsdiary.sdk.exception.UnauthorizedException
import ru.unit.barsdiary.ui.InAppNotifications
import ru.unit.barsdiary.ui.fragment.dialog.InfoDialogFragment
import ru.unit.barsdiary.ui.viewmodel.MainViewModel
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity(@LayoutRes val res: Int) : AppCompatActivity(res) {

    companion object {
        private const val INFO_DIALOG_TAG = "infoDialog"
    }

    private var _infoDialog: InfoDialogFragment? = null
    val infoDialog get() = _infoDialog

    private val infoDialogMutex = Mutex()

    private val infoDialogTag = INFO_DIALOG_TAG + this::class.java.simpleName

    @Inject
    lateinit var productFlavorDevelopingInterface: ProductFlavorDevelopingInterface

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    @Inject
    lateinit var inAppNotifications: InAppNotifications

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runCatching {
            window.decorView.rootView.let {
                productFlavorDevelopingInterface.removeStatusBar(this, it)
            }
        }

        val model = ViewModelProvider(this)[MainViewModel::class.java]

        inAppNotifications.init()

        model.throwableLiveData.observe(this) {
            when {
                it is UnauthorizedException -> {
                    model.handleUnauthorizedException()
                    if (this is StartActivity) {
                        showInfoDialog(getString(R.string.unauthorized), getString(R.string.unauthorized_error_text))
                    } else {
                        switchActivity(baseContext, StartActivity::class.java, Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                }
                it is FinishRegistrationAccountException -> {
                    showInfoDialog(getString(R.string.finish_registration_error), getString(R.string.finish_registration_error_text).format(it.site))
                }
                it is SocketTimeoutException -> { /* ignore */ }
                model.isOnline() -> {
                    showInfoDialog(getString(R.string.internal_error), it?.message ?: getString(R.string.internal_error_text))
                }
                else -> {
                    showInfoDialog(getString(R.string.no_internet), getString(R.string.no_internet_error_text))
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            settingsDataStore.flow.collectLatest { data ->
                if (data.syncWithSystemTheme == null || data.syncWithSystemTheme == true) {
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                } else if (data.nightTheme == true) {
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
                }
            }
        }
    }

    private fun showInfoDialog(title: String, text: String) {
        if (settingsDataStore.errorDialogs || this is StartActivity) {
            lifecycleScope.launch(Dispatchers.Main) {
                runCatching {
                    infoDialogMutex.withLock {
                        _infoDialog = supportFragmentManager.findFragmentByTag(infoDialogTag) as? InfoDialogFragment ?: InfoDialogFragment()

                        val dialog = _infoDialog
                        if (dialog != null) {
                            if (dialog.isAdded) {
                                return@withLock
                            }

                            dialog.arguments = InfoDialogFragment.config(title, text)
                            dialog.show(supportFragmentManager, infoDialogTag)
                        }
                    }
                }.onFailure {
                    Timber.e(it)
                }
            }
        }
    }

}