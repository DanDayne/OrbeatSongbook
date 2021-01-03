package com.dandayne.orbeatsongbook

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.dandayne.orbeatsongbook.db.AppDatabase
import com.dandayne.orbeatsongbook.db.DatabaseManager
import com.dandayne.orbeatsongbook.storage.StorageManager
import com.dandayne.orbeatsongbook.sync.PeriodicalNetworkMonitor
import com.dandayne.orbeatsongbook.sync.ReactiveNetworkMonitor
import com.dandayne.orbeatsongbook.sync.SyncManager
import com.dandayne.orbeatsongbook.ui.files.FilesDataHolder
import com.dandayne.orbeatsongbook.ui.pdf.PdfDataHolder
import com.dandayne.orbeatsongbook.ui.setlists.SetlistsDataHolder
import com.dandayne.orbeatsongbook.ui.settings.NightModeController
import com.dandayne.orbeatsongbook.utils.LiveDataHelper
import com.dandayne.orbeatsongbook.utils.extensions.isNightModeEnabled
import com.dandayne.orbeatsongbook.utils.extensions.isNightModeForced
import com.dandayne.orbeatsongbook.utils.extensions.saveNightModeSwitch
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.ext.android.startKoin
import com.dandayne.orbeatsongbook.utils.extensions.setNightMode as setNightModeUtil
import org.koin.dsl.module.module

@ObsoleteCoroutinesApi
class App : Application(), NightModeController {

    private val applicationModule = module(override = true) {

        single { DatabaseManager() }
        single { SyncManager() }
        single { LiveDataHelper() }
        single { FilesDataHolder() }
        single { PdfDataHolder() }
        single { SetlistsDataHolder() }
        single { applicationContext.resources }
        single { StorageManager() }
        single {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                ReactiveNetworkMonitor(applicationContext)
            else PeriodicalNetworkMonitor(applicationContext)
        }
        single { getSystemService(Context.ALARM_SERVICE) as AlarmManager }
        single("APPLICATION_CONTEXT") { applicationContext }
        single { (get("DATABASE") as AppDatabase).setlistDao() }
        single("DATABASE") {
            Room.databaseBuilder(
                this@App,
                AppDatabase::class.java,
                AppDatabase.DATABASE_NAME
            ).build()
        }
        single<NightModeController> { this@App }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(applicationModule))
        resolveNightMode()
    }

    override fun setNightMode(switch: Boolean) {
        saveNightModeSwitch(switch)
        setNightModeUtil(switch)
    }

    override fun isNightModeEnabled() = (this as Context).isNightModeEnabled()

    override fun isNightModeForced() = (this as Context).isNightModeForced()

    override fun resolveNightMode() {
        setNightMode(isNightModeEnabled())
    }
}