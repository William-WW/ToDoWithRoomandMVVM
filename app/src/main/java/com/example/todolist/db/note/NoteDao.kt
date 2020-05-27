package com.example.todolist.db.note

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.db.note.Note

@Dao
interface NoteDao {
    @Query("Select * from note")
    fun getNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)
}