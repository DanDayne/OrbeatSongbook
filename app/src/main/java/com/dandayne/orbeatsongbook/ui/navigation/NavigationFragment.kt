package com.dandayne.orbeatsongbook.ui.navigation

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.databinding.FragmentNavigationBinding
import com.dandayne.orbeatsongbook.ui.pdf.PdfDataHolder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject

class NavigationFragment : Fragment(), NavigationController {

    private val viewModel: NavigationViewModel by viewModels()
    private lateinit var dataBinding: FragmentNavigationBinding

    private val pdfDataHolder: PdfDataHolder by inject()

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var tabLayout: TabLayout

    companion object {
        const val TAG = "navigationFragment"
        const val LAST_PAGE = "last_page"
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
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_navigation, container, false
        )
        dataBinding.viewModel = viewModel
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = NavigationPagerAdapter(requireActivity())
        viewPager.offscreenPageLimit = 3

        tabLayout = view.findViewById(R.id.tab_layout)
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text =
                context?.getString(NavigationPagerAdapter.getFragmentNameByPosition(position))
        }
        tabLayoutMediator.attach()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.currentPage = position
            }
        })

        pdfDataHolder.openedFile.observe(viewLifecycleOwner) {
            if (!it.hasBeenHandled) {
                it.handle()
                viewPager.currentItem = 2
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
            requireContext().getSharedPreferences(TAG, MODE_PRIVATE).edit(commit = true) {
                putInt(LAST_PAGE, it)
            }
            super.onPause()
        }
    }
}