package com.dandayne.orbeatsongbook.db

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.room.*
import com.dandayne.orbeatsongbook.db.model.File
import com.dandayne.orbeatsongbook.db.model.Setlist
import com.dandayne.orbeatsongbook.db.model.SetlistFileCrossRef
import com.dandayne.orbeatsongbook.db.model.SetlistWithFiles

@Dao
interface SetlistDao {

    @Query("SELECT * FROM Setlist")
    fun getSetlists(): LiveData<List<Setlist>>

    @Query("SELECT * FROM File")
    fun getFiles(): LiveData<List<File>>

    @Query("SELECT * FROM SetlistFileCrossRef WHERE setlistId = :setlistId AND uri = :fileUri")
    suspend fun fetchFileSetlistCrossRef(fileUri: Uri, setlistId: Int): SetlistFileCrossRef?

    @Query("SELECT * FROM SETLISTFILECROSSREF WHERE setlistId = :setlistId ORDER BY `order`")
    fun getAllFileSetlistCrossRefs(setlistId: Int): LiveData<List<SetlistFileCrossRef>>

    @Query("UPDATE setlistfilecrossref SET `order` = `order` + :difference WHERE setlistId = :setlistId AND `order` >= :position;")
    suspend fun incrementSetlistFileCrossRefsAtPosition(setlistId: Int, position: Int, difference: Int)

    @Transaction
    @Query("SELECT * FROM Setlist WHERE setlistName = :setlistName")
    suspend fun fetchSetlistByName(setlistName: String): Setlist

    @Transaction
    @Query("SELECT * FROM Setlist WHERE setlistId = :setlistId")
    suspend fun fetchSetlistWithFilesByName(setlistId: Int): SetlistWithFiles

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFileToSetlist(setlistFileCrossRef: SetlistFileCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: File)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetlist(setlist: Setlist): Long

    @Delete
    suspend fun deleteFileFromSetlist(setlistFileCrossRef: SetlistFileCrossRef)

    @Delete
    suspend fun deleteFile(file: File)

    @Delete
    suspend fun deleteSetlist(setlist: Setlist)

    @Transaction
    @Query("DELETE FROM SetlistFileCrossRef WHERE setlistId = :setlistId")
    suspend fun deleteAllSetlistFileCrossRefsBySetlistId(setlistId: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFile(file: File)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSetlist(setlist: Setlist)

    @Transaction
    @Query("SELECT MAX(`order`) FROM SETLISTFILECROSSREF WHERE setlistId = :setlistId")
    suspend fun getMaxOrderOfSetlist(setlistId: Int): Int?

    @Transaction
    @Query("SELECT * FROM SetlistFileCrossRef WHERE setlistId = :setlistId AND `order` = :position")
    suspend fun fetchSetlistFileCrossRefByPosition(setlistId: Int, position: Int): SetlistFileCrossRef?

    @Transaction
    @Query("SELECT * FROM FILE WHERE uri = :fileUri")
    suspend fun fetchFileByUri(fileUri: Uri): File

    @Transaction
    @Query("SELECT * FROM Setlist WHERE setlistId = :setlistId")
    suspend fun fetchSetlistById(setlistId: Int): Setlist?
}