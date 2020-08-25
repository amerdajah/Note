package com.amerdev.note.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class Note(
    var title: String,
    var content: String,
    var timeStamp: String
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    init {
        id = 0
    }


    override fun toString(): String {
        return "Note(id=$id, title='$title', content='$content', timeStamp='$timeStamp')"
    }
}

