package com.example.groupviewer.db.model

import android.net.Uri
import androidx.room.*

@Entity
data class Setlist(
    @PrimaryKey(autoGenerate = true) val setlistId: Int,
    val setlistName: String
) {
    constructor(setlistName: String) : this(0, setlistName)
}
@Entity
data class File(
    val fileName: String,
    @PrimaryKey val uri: Uri
)

@Entity(primaryKeys = ["setlistId", "uri"])
data class SetlistFileCrossRef(
    val setlistId: Int,
    val uri: Uri
)
data class SetlistWithFiles(
    @Embedded val setlist: Setlist,
    @Relation(
        parentColumn = "setlistId",
        entityColumn = "uri",
        associateBy = Junction(SetlistFileCrossRef::class)
    )
    val files: List<File>
)
data class FileWithSetlists(
    @Embedded val file: File,
    @Relation(
        parentColumn = "uri",
        entityColumn = "setlistId",
        associateBy = Junction(SetlistFileCrossRef::class)
    )
    val setlists: List<Setlist>
)