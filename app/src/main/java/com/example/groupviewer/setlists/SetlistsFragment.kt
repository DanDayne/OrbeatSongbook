package com.example.groupviewer.setlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.groupviewer.R
import com.example.groupviewer.databinding.FragmentSetlistsBinding

class SetlistsFragment : Fragment() {
    private val viewModel: SetlistsViewModel by viewModels()
    private lateinit var dataBinding: FragmentSetlistsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_setlists, container, true
        )
        dataBinding.viewModel = viewModel
        return dataBinding.root
    }
}