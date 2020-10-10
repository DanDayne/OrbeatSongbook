package com.example.groupviewer.pdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.groupviewer.R
import com.example.groupviewer.databinding.FragmentPdfBinding

class PdfFragment: Fragment() {
    private val viewModel: PdfViewModel by viewModels()
    private lateinit var dataBinding: FragmentPdfBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pdf, container, true
        )
        dataBinding.viewModel = viewModel
        return dataBinding.root
    }
}