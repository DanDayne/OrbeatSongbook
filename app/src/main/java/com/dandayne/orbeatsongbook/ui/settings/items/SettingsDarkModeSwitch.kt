package com.dandayne.orbeatsongbook.ui.settings.items

import android.content.Context
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.ui.settings.NightModeController
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SettingsDarkModeSwitch : SettingsSwitch(),
    KoinComponent {

    private val nightModeController: NightModeController by inject()
    private val context: Context by inject("APPLICATION_CONTEXT")

    companion object {
        const val NIGHT_MODE = "NIGHT_MODE"
    }

    override val title: String = context.getString(R.string.dark_mode_title)
    override val subTitle: String = context.getString(R.string.dark_mode_text)
    override val textOn: String = context.getString(R.string.on)
    override val textOff: String = context.getString(R.string.auto)
    override val getValueBlock: () -> Boolean = { nightModeController.isNightModeForced() }
    override val setValueBlock: (Boolean) -> Unit = { nightModeController.setNightMode(it) }
}