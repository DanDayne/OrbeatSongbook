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
import com.dandayne.orbeatsongbook.databinding.FragmentDialogRenameSetlistBinding

class SetlistRenameDialogFragment: DialogFragment() {

    companion object {
        const val TAG = "setlistRenameDialog"
        private const val SETLIST_KEY = "setlistKey"

        fun newInstance(setlistId: Int?):SetlistRenameDialogFragment {
            val args = Bundle()
            setlistId?.let { args.putInt(SETLIST_KEY, setlistId) }
            return SetlistRenameDialogFragment().apply { arguments = args }
        }
    }

    private val viewModel: SetlistRenameDialogViewModel by viewModels()
    private lateinit var dataBinding: FragmentDialogRenameSetlistBinding

    private val closeObserver = Observer<Boolean> {
        if (it) dismiss()
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
            inflater, R.layout.fragment_dialog_rename_setlist, container, true
        )
        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.close.observe(this, closeObserver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.setlistId = arguments?.getInt(SETLIST_KEY)
        super.onCreate(savedInstanceState)
    }
}