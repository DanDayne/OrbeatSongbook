package com.dandayne.orbeatsongbook.utils.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

inline fun <reified T : Fragment> FragmentActivity.getOrCreateFragment(tag: String = T::class.java.name): T =
    (supportFragmentManager.findFragmentByTag(tag) as? T) ?: T::class.java.newInstance()