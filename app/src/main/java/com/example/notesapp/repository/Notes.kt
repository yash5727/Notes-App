package com.example.notesapp.repository

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var title: String,
    var description: String,
    var isLocked: Boolean = false
) : Parcelable
