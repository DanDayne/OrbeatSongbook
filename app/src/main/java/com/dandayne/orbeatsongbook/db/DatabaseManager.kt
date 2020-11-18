package com.dandayne.orbeatsongbook.db

import android.net.Uri
import androidx.lifecycle.LiveData
import com.dandayne.orbeatsongbook.db.model.File
import com.dandayne.orbeatsongbook.db.model.Setlist
import com.dandayne.orbeatsongbook.db.model.SetlistFileCrossRef
import com.dandayne.orbeatsongbook.db.model.SetlistWithFiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class DatabaseManager : KoinComponent {

    private val dao: SetlistDao by inject()

    val files: LiveData<List<File>> = dao.getFiles()
    val setlists: LiveData<List<Setlist>> = dao.getSetlists()


    fun newFile(name: String, uri: Uri): File {
        val file = File(name, uri)
        GlobalScope.launch(Dispatchers.IO) { dao.insertFile(file) }
        return file
    }

    suspend fun fetchFileSetlistCrossRef(setlistId: Int, fileUri: Uri) =
        dao.fetchFileSetlistCrossRef(fileUri, setlistId)

    suspend fun fetchSetlistWithFiles(setlistId: Int): SetlistWithFiles =
        dao.fetchSetlistWithFilesByName(setlistId)

    fun newSetlist(name: String): Setlist? {
        if (name.isEmpty()) return null

        val setlist = Setlist(name)
        setlists.value?.forEach { if (it.setlistName == name) return null }

        GlobalScope.launch(Dispatchers.IO) { dao.insertSetlist(setlist) }
        return setlist
    }

    fun addFileToSetlist(fileUri: Uri, setlist: Setlist) =
        GlobalScope.launch(Dispatchers.IO) {
            if (dao.fetchFileSetlistCrossRef(fileUri, setlist.setlistId) == null) {
                val order = (dao.getMaxOrderOfSetlist(setlist.setlistId) ?: -1) + 1
                dao.addFileToSetlist(SetlistFileCrossRef(setlist.setlistId, fileUri, order))
            }
        }

    fun addFileToSetlist(fileUri: Uri, setlistId: Int) =
        GlobalScope.launch(Dispatchers.IO) {
            if (dao.fetchFileSetlistCrossRef(fileUri, setlistId) == null) {
                val order = (dao.getMaxOrderOfSetlist(setlistId) ?: -1) + 1
                dao.addFileToSetlist(SetlistFileCrossRef(setlistId, fileUri, order))
            }
        }

    fun addFilesToSetlist(fileUris: List<Uri>, setlistId: Int) =
        GlobalScope.launch(Dispatchers.IO) {
            fileUris.forEach {
                if (dao.fetchFileSetlistCrossRef(it, setlistId) == null) {
                    val order = (dao.getMaxOrderOfSetlist(setlistId) ?: -1) + 1
                    dao.addFileToSetlist(SetlistFileCrossRef(setlistId, it, order))
                }
            }
        }

    fun reorderSetlistByDragAndDrop(setlistId: Int, startPosition: Int, endPosition: Int) =
        GlobalScope.launch(Dispatchers.IO) {
            dao.fetchSetlistFileCrossRefByPosition(setlistId, startPosition)?.let {
                dao.deleteFileFromSetlist(it)
                dao.incrementSetlistFileCrossRefsAtPosition(setlistId, startPosition, -1)
                dao.incrementSetlistFileCrossRefsAtPosition(setlistId, endPosition, 1)
                dao.addFileToSetlist(SetlistFileCrossRef(it.setlistId, it.uri, endPosition))
            }
        }

    fun deleteFile(file: File) {
        GlobalScope.launch(Dispatchers.IO) { dao.deleteFile(file) }
    }

    private fun deleteSetlist(setlist: Setlist) {
        GlobalScope.launch(Dispatchers.IO) {
            dao.deleteSetlist(setlist)
            dao.deleteAllSetlistFileCrossRefsBySetlistId(setlist.setlistId)
        }
    }

    fun deleteSetlist(setlistId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            dao.fetchSetlistById(setlistId)?.let {
                deleteSetlist(it)
            }
        }
    }

    fun removeFileFromSetlist(fileUri: Uri, setlistId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            dao.fetchFileSetlistCrossRef(fileUri, setlistId)?.let {
                dao.deleteFileFromSetlist(it)
                dao.incrementSetlistFileCrossRefsAtPosition(setlistId, it.order, -1)
            }
        }
    }

    fun removeFilesFromSetlist(fileUris: List<Uri>, setlistId: Int) =
        GlobalScope.launch(Dispatchers.IO) {
            fileUris.forEach { uri ->
                dao.fetchFileSetlistCrossRef(uri, setlistId)?.let {
                    dao.deleteFileFromSetlist(it)
                    dao.incrementSetlistFileCrossRefsAtPosition(setlistId, it.order, -1)
                }
            }
        }

    fun renameSetlist(setlistId: Int, name: String): Boolean {
        if (name.isEmpty()) return false
        val newSetlist = Setlist(setlistId, name)
        setlists.value?.forEach {
            if (it.setlistName == newSetlist.setlistName) {
                return false
            }
        }
        GlobalScope.launch(Dispatchers.IO) {
            dao.updateSetlist(newSetlist)
        }
        return true
    }
}