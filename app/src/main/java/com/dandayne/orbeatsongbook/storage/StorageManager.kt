package com.dandayne.orbeatsongbook.storage

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.dandayne.orbeatsongbook.R
import com.dandayne.orbeatsongbook.db.DatabaseManager
import com.dandayne.orbeatsongbook.utils.extensions.getAllStoragePaths
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.io.File

class StorageManager : KoinComponent {

    private val context: Context by inject("APPLICATION_CONTEXT")
    private val databaseManager: DatabaseManager by inject()

    companion object {
        private const val EXTENSION = "pdf"
        const val EXTENSION_FOR_PUBLIC = ".pdf"
    }

    fun reloadFiles() {
        val allFileUris = mutableSetOf<Uri>()
        context.getAllStoragePaths().forEach { storagePath ->
            val sourcePath = File(storagePath, context.getString(R.string.app_name))
            if (sourcePath.exists()) {
                val files =
                    sourcePath.listFiles()?.filter { file -> file.extension == EXTENSION }
                        ?: listOf()
                files.forEach { file ->
                    file.toUri().apply {
                        databaseManager.newFile(file.nameWithoutExtension, this)
                        allFileUris.add(this)
                    }
                }
            }
        }
        databaseManager.files.value?.forEach { file ->
            if (!allFileUris.contains(file.uri)) databaseManager.deleteFile(file)
        }
    }
}

