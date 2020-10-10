package com.example.groupviewer.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.groupviewer.db.model.*


@Database(entities = [Setlist::class, File::class, SetlistFileCrossRef::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "setlists"
    }
    abstract fun setlistDao(): SetlistDao


}
