package com.dandayne.orbeatsongbook.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.ui.files.FilesFragment
import com.dandayne.orbeatsongbook.ui.pdf.PdfFragment
import com.dandayne.orbeatsongbook.ui.setlists.SetlistsFragment
import com.dandayne.orbeatsongbook.utils.extensions.getOrCreateFragment
import java.lang.IllegalStateException

class NavigationPagerAdapter(private val fa: FragmentActivity) : FragmentStateAdapter(fa) {

    class Tab(
        @DrawableRes val icon: Int?,
        @StringRes val title: Int?,
        val fragmentTag: String,
    )

    override fun getItemCount(): Int = tabs.size

    companion object {
        val tabs = listOf(
            Tab(null, R.string.files, FilesFragment::class.java.name),
            Tab(null, R.string.setlists, SetlistsFragment::class.java.name),
            Tab(null, R.string.viewer, PdfFragment::class.java.name),
        )

        fun getTabPositionByTag(tag: String) = tabs.indexOf(
            tabs.firstOrNull() { tab -> tab.fragmentTag == tag }
        )

        fun getTabByPosition(position: Int): Tab {
            return tabs[position]
        }
    }

    override fun createFragment(position: Int): Fragment {
        return resolveFragmentByTag(tabs[position].fragmentTag)
    }

    private fun resolveFragmentByTag(tag: String): Fragment {
        return when (tag) {
            FilesFragment::class.java.name -> fa.getOrCreateFragment<FilesFragment>()
            SetlistsFragment::class.java.name -> fa.getOrCreateFragment<SetlistsFragment>()
            PdfFragment::class.java.name -> fa.getOrCreateFragment<PdfFragment>()
            else -> throw IllegalStateException()
        }

    }
}