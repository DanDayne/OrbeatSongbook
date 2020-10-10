package com.example.groupviewer.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.groupviewer.R
import com.example.groupviewer.files.FilesFragment
import com.example.groupviewer.pdf.PdfFragment
import com.example.groupviewer.setlists.SetlistsFragment


class NavigationPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 3

    companion object {
        fun getFragmentNameByPosition(position: Int): Int {
            return when (position) {
                0 -> R.string.files
                1 -> R.string.setlists
                2 -> R.string.pdf
                else -> throw IllegalStateException()
            }
        }
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FilesFragment()
            1 -> SetlistsFragment()
            2 -> PdfFragment()
            else -> throw IllegalStateException()
        }
    }

}