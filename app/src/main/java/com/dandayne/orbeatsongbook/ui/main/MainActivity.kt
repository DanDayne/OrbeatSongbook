package com.dandayne.orbeatsongbook.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.databinding.ActivityMainBinding
import com.dandayne.orbeatsongbook.sync.MessageDisplay
import com.dandayne.orbeatsongbook.ui.navigation.NavigationController
import com.dandayne.orbeatsongbook.ui.setlists.NavigationObserver
import com.dandayne.orbeatsongbook.ui.settings.SettingsActivity
import com.dandayne.permission.ui.PermissionsDialog
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.standalone.KoinComponent


class MainActivity : AppCompatActivity(), MessageDisplay, KoinComponent, NavigationController,
    NavigationObserver, ActionBarController {
    private lateinit var viewModel: MainViewModel

    private lateinit var dataBinding: ActivityMainBinding

    companion object {
        private const val SETTINGS_REQUEST_CODE = 1234
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.header_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_button -> {
                startActivityForResult(
                    Intent(this, SettingsActivity::class.java),
                    SETTINGS_REQUEST_CODE
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @ObsoleteCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataBinding.viewModel = viewModel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PermissionsDialog().show(supportFragmentManager, PermissionsDialog.TAG)
        viewModel.setupSyncManager(this, this)
        viewModel.message.observe(this) {
            it?.let { showMessage(it) }
        }

        viewModel.actionBarTitle.observe(this) {
            it?.let { showSetlistNameOnActionBar(it) } ?: hideSetlistNameFromActionBar()
        }
    }

    override fun showMessage(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
    }

    override fun toggleNavigationBar(switch: Boolean) {
        supportActionBar?.apply { if (switch) show() else hide() }
        (supportFragmentManager.findFragmentById(R.id.navigation_fragment) as? NavigationController)
            ?.toggleNavigationBar(switch)
    }

    override fun showSetlistNameOnActionBar(setlistName: String) {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            title = setlistName
        }
    }

    override fun hideSetlistNameFromActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeButtonEnabled(false)
            title = getString(R.string.app_name)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        viewModel.closeSetlist()
        return true
    }

    override fun setIsOnSetlistFragment(value: Boolean) {
        viewModel.setIsOnSetlistFragment(value)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == Activity.RESULT_OK)
            recreate() else super.onActivityResult(requestCode, resultCode, data)
    }
}