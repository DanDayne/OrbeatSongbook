package com.dandayne.orbeatsongbook.ui.settings.items

import com.dandayne.orbeatsongbook.ui.settings.NightModeController

class SettingsDarkModeSwitch(nightModeController: NightModeController) : SettingsSwitch() {

    companion object {
        const val NIGHT_MODE = "NIGHT_MODE"
    }

    override val title: String = "Dark Mode"
    override val subTitle: String = "Enable dark mode"
    override val textOn: String = "On"
    override val textOff: String = "Auto"
    override val getValueBlock: () -> Boolean = { nightModeController.isNightModeEnabled() }
    override val setValueBlock: (Boolean) -> Unit = { nightModeController.setNightMode(it) }
}