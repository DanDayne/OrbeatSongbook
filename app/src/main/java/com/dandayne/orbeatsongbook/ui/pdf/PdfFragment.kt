package com.dandayne.orbeatsongbook.ui.pdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.databinding.FragmentPdfBinding
import com.dandayne.orbeatsongbook.db.model.File
import com.dandayne.orbeatsongbook.ui.navigation.NavigationController
import com.dandayne.orbeatsongbook.utils.extensions.isUsingNightModeResources
import kotlinx.android.synthetic.main.fragment_pdf.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PdfFragment : Fragment() {
    private val viewModel: PdfViewModel by viewModels()
    private lateinit var dataBinding: FragmentPdfBinding

    private val pdfObserver = Observer<FileToLaunch> { fileToLaunch ->
        fileToLaunch?.file?.let { loadPDFView(it) }
    }

    private val infoVisibilityObserver = Observer<Boolean> {
        if (it == true) {
            dataBinding.pdfName.visibility = View.VISIBLE
            (requireActivity() as? NavigationController)?.toggleNavigationBar(true)
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        } else {
            dataBinding.pdfName.visibility = View.GONE
            (requireActivity() as? NavigationController)?.toggleNavigationBar(false)
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    private val nextVisibilityObserver = Observer<Boolean?> {
        dataBinding.buttonNext.visibility = if (it == true) View.VISIBLE else View.GONE
    }

    private val previousVisibilityObserver = Observer<Boolean?> {
        dataBinding.buttonPrevious.visibility = if (it == true) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_pdf, container, true)
        dataBinding.viewModel = viewModel
        dataBinding.pdfName.bringToFront()

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.openedFile.observe(viewLifecycleOwner, pdfObserver)
        viewModel.hasNext.observe(viewLifecycleOwner, nextVisibilityObserver)
        viewModel.hasPrevious.observe(viewLifecycleOwner, previousVisibilityObserver)
        viewModel.isInfoVisible.observe(viewLifecycleOwner, infoVisibilityObserver)
    }

    private fun loadPDFView(file: File) {
        pdf_viewer.visibility = View.VISIBLE
        background_filler.visibility = View.GONE
        dataBinding.pdfName.text = file.fileName
        pdf_viewer.fromUri(file.uri).nightMode(requireContext().isUsingNightModeResources())
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .onDraw { _, _, _, _ -> }
            .onDrawAll { _, _, _, _ -> }
            .onPageChange { _, _ -> }
            .onTap {
                viewModel.changeInfoVisibility()
                true
            }
            .enableAnnotationRendering(true)
            .load()
    }

    override fun onPause() {
        super.onPause()
        viewModel.infoToPermanentlyVisible()
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch {  }
        viewModel.changeInfoVisibilityWithDelay()
    }
}