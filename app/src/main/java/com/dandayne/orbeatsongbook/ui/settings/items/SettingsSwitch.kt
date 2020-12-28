package com.dandayne.orbeatsongbook.ui.settings.items

abstract class SettingsSwitch : SettingsItem {
    abstract val title: String
    abstract val subTitle: String
    abstract val textOn: String
    abstract val textOff: String
    abstract val getValueBlock: () -> Boolean
    abstract val setValueBlock: (Boolean) -> Unit
}