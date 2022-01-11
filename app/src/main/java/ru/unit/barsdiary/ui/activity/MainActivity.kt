package ru.unit.barsdiary.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
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
import ru.unit.barsdiary.ui.fragment.AuthFragment
import ru.unit.barsdiary.ui.fragment.dialog.InfoDialogFragment
import ru.unit.barsdiary.ui.viewmodel.MainViewModel
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        private const val INFO_DIALOG_TAG = "infoDialog"
    }

    private var _infoDialog: InfoDialogFragment? = null

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

        val appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                runCatching {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this,
                        11
                    )
                }
            }

            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                runCatching {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.update_downloaded),
                        Snackbar.LENGTH_INDEFINITE
                    ).apply {
                        val params = view.layoutParams as? FrameLayout.LayoutParams
                        params?.gravity = Gravity.TOP
                        view.layoutParams = params

                        setAction(getString(R.string.reload)) { appUpdateManager.completeUpdate() }
                        show()
                    }
                }
            }
        }.addOnFailureListener { Timber.e(it) }

        model.throwableLiveData.observe(this) {
            val isAuthFragment = supportFragmentManager.fragments.find { it is AuthFragment } != null

            when {
                it is UnauthorizedException -> {
                    model.handleUnauthorizedException()
                    if (isAuthFragment) {
                        showInfoDialog(getString(R.string.unauthorized), getString(R.string.unauthorized_error_text), true)
                    } else {
                        model.throwableLiveData.removeObservers(this)
                        switchActivity(baseContext, MainActivity::class.java, Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                }
                it is FinishRegistrationAccountException -> {
                    showInfoDialog(
                        getString(R.string.finish_registration_error),
                        getString(R.string.finish_registration_error_text).format(it.site),
                        isAuthFragment
                    )
                }
                it is SocketTimeoutException -> { /* ignore */
                }
                it is SSLHandshakeException -> {
                    showInfoDialog(getString(R.string.ssl_handshake), getString(R.string.set_correct_date), isAuthFragment)
                }
                model.isOnline() -> {
                    showInfoDialog(getString(R.string.internal_error), it?.message ?: getString(R.string.internal_error_text), isAuthFragment)
                }
                else -> {
                    showInfoDialog(getString(R.string.no_internet), getString(R.string.no_internet_error_text), isAuthFragment)
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

    private fun showInfoDialog(title: String, text: String, forceShow: Boolean = false) {
        if (settingsDataStore.errorDialogs || forceShow) {
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