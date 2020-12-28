package com.dandayne.orbeatsongbook.ui.settings

interface NightModeController {
    fun resolveNightMode()
    fun setNightMode(switch: Boolean)
    fun isNightModeEnabled(): Boolean
}