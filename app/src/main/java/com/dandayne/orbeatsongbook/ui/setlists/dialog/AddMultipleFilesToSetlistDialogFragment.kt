package com.dandayne.orbeatsongbook.ui.setlists.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.db.DatabaseManager
import com.dandayne.orbeatsongbook.ui.setlists.SetlistsDataHolder
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.text.Normalizer
import java.util.*

class AddMultipleFilesToSetlistDialogFragment : DialogFragment(), KoinComponent {

    private val databaseManager: DatabaseManager by inject()
    private val setlistsDataHolder: SetlistsDataHolder by inject()

    private val files = (databaseManager.files.value?.sortedBy {
        Normalizer.normalize(
            it.fileName.toLowerCase(Locale.ROOT),
            Normalizer.Form.NFD
        )
    } ?: listOf())

    private val checked = setlistsDataHolder.showingSetlistContent.value?.files?.let {
        files.map { file -> it.contains(file) }.toBooleanArray()
    } ?: booleanArrayOf()

    companion object {
        const val TAG = "addMultipleFilesToSetlistDialogFragment"
        private const val SETLIST_ID_KEY = "setlistIdKey"
        private const val SETLIST_NAME_KEY = "setlistNameKey"

        fun newInstance(
            setlistId: Int,
            setlistName: String
        ): AddMultipleFilesToSetlistDialogFragment {
            val args = Bundle()
            args.putInt(SETLIST_ID_KEY, setlistId)
            args.putString(SETLIST_NAME_KEY, setlistName)
            val fragment = AddMultipleFilesToSetlistDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var setlistId: Int? = null
    var setlistName: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(setlistName)
            .setMultiChoiceItems(
                files.map { it.fileName }.toTypedArray(), checked
            ) { _, which, isChecked -> checked[which] = isChecked }
            .setPositiveButton(R.string.ok) { _, _ -> }
            .setNegativeButton(R.string.cancel) { _, _ -> dismiss() }
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setlistId = arguments?.getInt(SETLIST_ID_KEY)
        setlistName = arguments?.getString(SETLIST_NAME_KEY)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (parentFragment as? DialogInterface.OnDismissListener)?.onDismiss(dialog)
    }

    override fun onResume() {
        super.onResume()
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val filesToAdd = mutableListOf<Uri>()
            val filesToRemove = mutableListOf<Uri>()
            checked.forEachIndexed { index, isChecked ->
                if (isChecked) filesToAdd.add(files[index].uri)
                else filesToRemove.add(files[index].uri)
            }
            setlistId?.let { setlistId ->
                databaseManager.addFilesToSetlist(filesToAdd, setlistId).invokeOnCompletion {
                    databaseManager.removeFilesFromSetlist(filesToRemove, setlistId)
                        .invokeOnCompletion { dismiss() }
                }
            }
        }
    }
}