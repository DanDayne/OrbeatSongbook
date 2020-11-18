package com.dandayne.orbeatsongbook.ui.setlists.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.ui.files.dialog.FileOptionsDialogFragment

class SetlistConfirmDeleteDialogFragment: DialogFragment() {
    private val viewModel: SetlistConfirmDeleteDialogViewModel by viewModels()

    companion object {
        const val TAG = "setlistConfirmDeleteDialog"
        private const val SETLIST_KEY = "setlistKey"

        fun newInstance(setlistId: Int?): SetlistConfirmDeleteDialogFragment {
            val args = Bundle()
            setlistId?.let { args.putInt(SETLIST_KEY, setlistId) }
            return SetlistConfirmDeleteDialogFragment().apply { arguments = args }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = requireContext()
        return AlertDialog.Builder(context)
            .setTitle(R.string.setlist_delete_warning)
            .setPositiveButton(R.string.ok) {_, _ ->
                viewModel.deleteSetlist()
            }.setNegativeButton(R.string.cancel) {_, _ ->
                dismiss()
            }
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.setlistId = arguments?.getInt(FileOptionsDialogFragment.SETLIST_KEY)
        super.onCreate(savedInstanceState)
    }
}