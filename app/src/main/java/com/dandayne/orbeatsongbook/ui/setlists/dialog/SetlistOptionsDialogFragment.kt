package com.dandayne.orbeatsongbook.ui.setlists.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.utils.DialogOption

class SetlistOptionsDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "SetlistOptionsDialogFragment"
        private const val SETLIST_ID_KEY = "setlistIdKey"
        private const val SETLIST_NAME_KEY = "setlistNameKey"

        fun newInstance(setlistId: Int, setlistName: String): SetlistOptionsDialogFragment {
            val args = Bundle()
            args.putInt(SETLIST_ID_KEY, setlistId)
            args.putString(SETLIST_NAME_KEY, setlistName)
            val fragment = SetlistOptionsDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var setlistId: Int? = null
    var setlistName: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val options = listOf(
            DialogOption(R.string.rename) {
                SetlistRenameDialogFragment.newInstance(setlistId)
                    .show(parentFragmentManager, SetlistRenameDialogFragment.TAG)
            },
            DialogOption(R.string.remove) {
                SetlistConfirmDeleteDialogFragment.newInstance(setlistId)
                    .show(parentFragmentManager, SetlistConfirmDeleteDialogFragment.TAG)
            }
        )
        return AlertDialog.Builder(requireContext())
            .setTitle(setlistName)
            .setItems(options.map { requireContext().getString(it.titleResId) }
                .toTypedArray()) { _, which ->
                options[which].action()
            }
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setlistId = arguments?.getInt(SETLIST_ID_KEY)
        setlistName = arguments?.getString(SETLIST_NAME_KEY)
    }
}