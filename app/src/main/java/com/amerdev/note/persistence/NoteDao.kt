package com.amerdev.note.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amerdev.note.models.Note

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNotes(notes: Note)

    @Query("SELECT * FROM notes")
    fun getNotes(): LiveData<List<Note>>

    @Query("select * from notes where title like :title")
    fun getNoteWithCustomQuery(title: String): List<Note>

    @Delete
    suspend fun deleteNotes(notes: Note)

    @Update
    suspend fun updateNotes(notes: Note)
}