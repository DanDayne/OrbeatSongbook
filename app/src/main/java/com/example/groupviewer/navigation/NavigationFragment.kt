package com.example.groupviewer.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.groupviewer.R
import com.example.groupviewer.databinding.FragmentNavigationBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class NavigationFragment : Fragment() {

    private val viewModel: NavigationViewModel by viewModels()
    private lateinit var dataBinding: FragmentNavigationBinding

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayoutMediator: TabLayoutMediator

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

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text =
                context?.getString(NavigationPagerAdapter.getFragmentNameByPosition(position))
        }
        tabLayoutMediator.attach()

        super.onViewCreated(view, savedInstanceState)
    }
}