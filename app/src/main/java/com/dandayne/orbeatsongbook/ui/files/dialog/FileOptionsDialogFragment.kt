package com.dandayne.orbeatsongbook.ui.files.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.storage.StorageManager
import com.dandayne.orbeatsongbook.utils.DialogOption

class FileOptionsDialogFragment : DialogFragment() {

    private val viewModel: FileOptionsDialogViewModel by viewModels()

    private lateinit var fileUri: Uri
    private var setlistId: Int? = null
    private var fileName: String? = null

    companion object {
        const val TAG = "FileOptionsDialogFragment"
        const val FILE_KEY = "fileKey"
        const val SETLIST_KEY = "setlistKey"

        fun newInstance(fileUri: Uri, setlistId: Int?): FileOptionsDialogFragment {
            val args = Bundle()
            args.putParcelable(FILE_KEY, fileUri)
            setlistId?.let { args.putInt(SETLIST_KEY, setlistId) }
            return FileOptionsDialogFragment().apply { arguments = args }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileUri = arguments?.get(FILE_KEY) as Uri
        setlistId = arguments?.get(SETLIST_KEY) as Int?
        fileName = fileUri.pathSegments[fileUri.pathSegments.size - 1]
            ?.removeSuffix(StorageManager.EXTENSION_FOR_PUBLIC)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val options = arrayListOf(
            DialogOption(R.string.add_to_setlist) {
                AddToSetlistDialogFragment.newInstance(fileUri)
                    .show(parentFragmentManager, AddToSetlistDialogFragment.TAG)
            }
        )

        setlistId?.let {
            options.add(
                DialogOption(R.string.remove_from_setlist) {
                    viewModel.removeFileFromSetlist(fileUri, it)
                }
            )
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(fileName)
            .setItems(options.map { requireContext().getString(it.titleResId) }
                .toTypedArray()) { _, which ->
                options[which].action()
            }

        return alertDialog.create()
    }
}