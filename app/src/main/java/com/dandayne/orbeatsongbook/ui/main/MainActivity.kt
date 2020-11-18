package com.dandayne.orbeatsongbook.ui.main

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.databinding.ActivityMainBinding
import com.dandayne.orbeatsongbook.db.DatabaseManager
import com.dandayne.orbeatsongbook.sync.MessageDisplay
import com.dandayne.orbeatsongbook.sync.SyncManager
import com.dandayne.orbeatsongbook.ui.navigation.NavigationController
import com.dandayne.orbeatsongbook.ui.pdf.PdfDataHolder
import com.dandayne.permission.extensions.areAllPermissionsGranted
import com.dandayne.orbeatsongbook.utils.extensions.restartActivity
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class MainActivity : AppCompatActivity(), MessageDisplay, KoinComponent, NavigationController {
    private val viewModel: MainViewModel by viewModels()
    private val pdfDataHolder: PdfDataHolder by inject()
    private val databaseManager: DatabaseManager by inject()
    private val syncManager: SyncManager by inject()
    private lateinit var dataBinding: ActivityMainBinding


    @ObsoleteCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataBinding.viewModel = viewModel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !areAllPermissionsGranted())
            com.dandayne.permission.ui.PermissionsDialogFragment()
                .show(supportFragmentManager, com.dandayne.permission.ui.PermissionsDialogFragment.TAG)
        syncManager.messageDisplay = this
        syncManager.start()
        syncManager.fileNameToLaunch.observe(this) {
            if (!it.hasBeenHandled) {
                databaseManager.files.value?.find { file -> file.fileName == it.name }
                    ?.let { file ->
                        pdfDataHolder.openFile(listOf(file), 0, sync = false)
                    } ?: showMessage(R.string.non_existent_file)
            }
        }
    }

    override fun showMessage(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
    }

    override fun toggleNavigationBar(switch: Boolean) {
        (supportFragmentManager.findFragmentById(R.id.navigation_fragment) as? NavigationController)
            ?.toggleNavigationBar(switch)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        restartActivity()
    }
}