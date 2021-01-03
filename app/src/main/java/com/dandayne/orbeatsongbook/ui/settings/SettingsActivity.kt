package com.dandayne.orbeatsongbook.ui.settings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.databinding.ActivitySettingsBinding
import com.dandayne.orbeatsongbook.ui.settings.items.SettingsDarkModeSwitch
import com.dandayne.orbeatsongbook.ui.settings.items.SettingsItem

class SettingsActivity : AppCompatActivity(), SettingsController {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var dataBinding: ActivitySettingsBinding

    private lateinit var settingItems: List<SettingsItem>

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingItems =
            listOf<SettingsItem>(SettingsDarkModeSwitch())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.settings)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        dataBinding.viewModel = viewModel
        dataBinding.settingsController = this
        dataBinding.versionName = getString(
            R.string.version,
            packageManager.getPackageInfo(packageName, 0).versionName
        )
        val settingsRecycler: RecyclerView = dataBinding.settingsAdapter
        val setlistsLayoutManager = LinearLayoutManager(this)
        settingsRecycler.layoutManager = setlistsLayoutManager
        val settingsAdapter = SettingsAdapter(settingItems)
        settingsRecycler.adapter = settingsAdapter
        settingsRecycler.addItemDecoration(
            DividerItemDecoration(
                settingsRecycler.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        super.onBackPressed()
    }
}