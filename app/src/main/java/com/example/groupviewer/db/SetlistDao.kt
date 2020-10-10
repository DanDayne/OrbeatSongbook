package com.example.groupviewer.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.groupviewer.db.model.File
import com.example.groupviewer.db.model.Setlist
import com.example.groupviewer.db.model.SetlistFileCrossRef
import com.example.groupviewer.db.model.SetlistWithFiles

@Dao
interface SetlistDao {

    @Query( "SELECT * FROM Setlist")
    fun getSetlists(): LiveData<List<Setlist>>

    @Query( "SELECT * FROM File")
    fun getFiles(): LiveData<List<File>>

    @Transaction
    @Query("SELECT * FROM Setlist WHERE setlistName = :setlistName")
    fun getSetlistByName(setlistName: String): Setlist

    @Transaction
    @Query("SELECT * FROM Setlist WHERE setlistName = :setlistName")
    fun getSetlistWithFilesByName(setlistName: String): SetlistWithFiles

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFileToSetlist(setlistFileCrossRef: SetlistFileCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFile(file: File)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSetlist(setlist: Setlist): Long

    @Delete
    fun deleteFileFromSetlist(setlistFileCrossRef: SetlistFileCrossRef)

    @Delete
    fun deleteFile(file: File)

    @Delete
    fun deleteSetlist(setlist: Setlist)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateFile(file: File)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSetlist(setlist: Setlist)

}