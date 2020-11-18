package com.dandayne.orbeatsongbook.ui.setlists.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.databinding.FragmentDialogNewSetlistBinding

class NewSetlistDialogFragment: DialogFragment() {
    private val viewModel: NewSetlistDialogViewModel by viewModels()
    private lateinit var dataBinding: FragmentDialogNewSetlistBinding

    private val closeObserver = Observer<Boolean> {
        if (it) dismiss()
    }

    companion object {
        const val TAG = "NewSetlistDialogFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dialog_new_setlist, container, true
        )
        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.close.observe(this, closeObserver)
    }
}