package ru.unit.barsdiary.mvvm.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.unit.barsdiary.R
import ru.unit.barsdiary.mvvm.viewmodel.MainViewModel
import ru.unit.barsdiary.other.function.switchActivity

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model = ViewModelProvider(this)[MainViewModel::class.java]

        lifecycleScope.launchWhenCreated {
            model.unauthorizedFlow.collectLatest {
                if (it) switchActivity(baseContext, StartActivity::class.java, Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }
}