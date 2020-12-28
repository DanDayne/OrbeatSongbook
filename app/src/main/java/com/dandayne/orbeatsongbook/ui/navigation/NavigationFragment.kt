package com.dandayne.orbeatsongbook.ui.navigation

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.databinding.FragmentNavigationBinding
import com.dandayne.orbeatsongbook.ui.pdf.PdfDataHolder
import com.dandayne.orbeatsongbook.ui.pdf.PdfFragment
import com.dandayne.orbeatsongbook.ui.setlists.NavigationObserver
import com.dandayne.orbeatsongbook.ui.setlists.SetlistsFragment
import com.dandayne.orbeatsongbook.ui.settings.SettingsActivity
import com.dandayne.orbeatsongbook.ui.settings.SettingsOpener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject

class NavigationFragment : Fragment(), NavigationController, SettingsOpener {

    private val viewModel: NavigationViewModel by viewModels()
    private lateinit var dataBinding: FragmentNavigationBinding

    private val pdfDataHolder: PdfDataHolder by inject()

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var tabLayout: TabLayout

    companion object {
        const val TAG = "navigationFragment"
        const val LAST_PAGE = "last_page"
        private const val SETTINGS_CODE = 1234
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.currentPage =
            requireContext().getSharedPreferences(TAG, MODE_PRIVATE).getInt(LAST_PAGE, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_navigation, container, false
        )
        dataBinding.viewModel = viewModel
        dataBinding.settingsOpener = this
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = dataBinding.pager
        viewPager.adapter = NavigationPagerAdapter(requireActivity())
        viewPager.offscreenPageLimit = NavigationPagerAdapter.tabs.size

        tabLayout = dataBinding.tabLayout
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            NavigationPagerAdapter.getTabByPosition(position).apply {
                title?.let { tab.text = requireContext().getString(it) }
                icon?.let { tab.icon = ContextCompat.getDrawable(requireContext(), it) }
            }

        }
        tabLayoutMediator.attach()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.currentPage = position
                (requireActivity() as? NavigationObserver)?.apply {
                    val setlistsFragmentPosition = NavigationPagerAdapter.getTabPositionByTag(
                        SetlistsFragment::class.java.name
                    )
                    setIsOnSetlistFragment(position == setlistsFragmentPosition)
                }
            }
        })

        pdfDataHolder.openedFile.observe(viewLifecycleOwner) {
            if (!it.hasBeenHandled) {
                it.handle()
                viewPager.currentItem =
                    NavigationPagerAdapter.getTabPositionByTag(PdfFragment::class.java.name)
            }
        }
        viewPager.isUserInputEnabled = false
        super.onViewCreated(view, savedInstanceState)
    }

    override fun toggleNavigationBar(switch: Boolean) {
        tabLayout.apply { visibility = if (switch) View.VISIBLE else View.GONE }
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentPage?.let { viewPager.currentItem = it }
    }


    override fun onPause() {
        viewModel.currentPage?.let {
            requireContext().getSharedPreferences(TAG, MODE_PRIVATE).edit {
                putInt(LAST_PAGE, it)
            }
            super.onPause()
        }
    }

    override fun openSettings() {
        startActivityForResult(
            Intent(requireContext(), SettingsActivity::class.java),
            SETTINGS_CODE
        )
    }
}