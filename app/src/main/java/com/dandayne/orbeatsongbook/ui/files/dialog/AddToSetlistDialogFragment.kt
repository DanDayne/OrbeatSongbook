package com.dandayne.orbeatsongbook.ui.files.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.db.DatabaseManager
import com.dandayne.orbeatsongbook.ui.setlists.SetlistsDataHolder
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class AddToSetlistDialogFragment : DialogFragment(), KoinComponent {
    private val databaseManager: DatabaseManager by inject()
    private val setlistsDataHolder: SetlistsDataHolder by inject()

    private lateinit var fileUri: Uri

    companion object {
        const val TAG = "addToSetlistDialog"
        private const val FILE_KEY = "fileKey"

        fun newInstance(fileUri: Uri): AddToSetlistDialogFragment {
            val args = Bundle()
            args.putParcelable(FILE_KEY, fileUri)
            return AddToSetlistDialogFragment().apply { arguments = args }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileUri = arguments?.get(FILE_KEY) as Uri
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val setlists = databaseManager.setlists.value ?: listOf()
        val title = if (setlists.isEmpty()) R.string.no_setlists else R.string.setlists
        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setItems(setlists.map { it.setlistName }.toTypedArray()) { _, which ->
                setlists.getOrNull(which)?.let {
                    databaseManager.addFileToSetlist(fileUri, it)
                        .invokeOnCompletion { setlistsDataHolder.refresh() }
                }
            }
            .create()
    }
}