package ru.unit.barsdiary.mvvm.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.unit.barsdiary.R
import ru.unit.barsdiary.mvvm.fragment.AuthFragment
import ru.unit.barsdiary.mvvm.fragment.dialog.InfoDialogFragment
import ru.unit.barsdiary.mvvm.fragment.dialog.ServerListDialogFragment
import ru.unit.barsdiary.mvvm.viewmodel.MainViewModel
import ru.unit.barsdiary.other.function.switchActivity
import ru.unit.barsdiary.sdk.exception.UnauthorizedException
import timber.log.Timber
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {
    companion object {
        private const val INFO_DIALOG_TAG = "infoDialogActivity"
    }

    private lateinit var infoDialog: InfoDialogFragment
    private val infoDialogMutex = Mutex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model = ViewModelProvider(this)[MainViewModel::class.java]

        model.throwableFlow.observe(this) {
            when {
                it is UnauthorizedException -> {
                    model.handleUnauthorizedException()

                    switchActivity(baseContext, StartActivity::class.java, Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                model.isOnline() -> {
                    showInfoDialog(getString(R.string.internal_error), it?.message ?: getString(R.string.internal_error_text))
                }
                else -> {
                    showInfoDialog(getString(R.string.no_internet), getString(R.string.no_internet_error_text))
                }
            }
        }
    }

    private fun showInfoDialog(title: String, text: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            infoDialogMutex.withLock {
                infoDialog = supportFragmentManager.findFragmentByTag(INFO_DIALOG_TAG) as? InfoDialogFragment ?: InfoDialogFragment()

                if(infoDialog.isAdded) {
                    return@withLock
                }

                infoDialog.send(title, text)
                infoDialog.show(supportFragmentManager, INFO_DIALOG_TAG)
            }
        }
    }
}