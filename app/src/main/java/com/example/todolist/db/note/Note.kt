package com.example.todolist.db.note

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "note")
    var note: String,
    @ColumnInfo(name = "waktubuat")
    var waktubuat: String,
    @ColumnInfo(name = "waktuupdate")
    var waktuupdate: String,
    @ColumnInfo(name = "tempo")
    val tempo: String,
    @ColumnInfo(name = "waktutempo")
    val waktutempo: String
) : Parcelable