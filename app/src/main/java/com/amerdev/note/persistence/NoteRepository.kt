package com.amerdev.note.persistence

import android.content.Context
import androidx.lifecycle.LiveData
import com.amerdev.note.models.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class NoteRepository(private val context: Context) {

    fun insertNoteTask(note: Note){
        CoroutineScope(IO).launch {
            NoteDatabase(context).getNoteDao()?.insertNotes(note)
        }
    }

    suspend fun updateNote(note: Note){
        context?.let { NoteDatabase(it).getNoteDao()?.updateNotes(note) }
    }

    fun retrieveNotesTask(): LiveData<List<Note>>{
        return NoteDatabase(context).getNoteDao()?.getNotes()!!
    }

    suspend fun deleteNote(note: Note){
        NoteDatabase(context).getNoteDao()?.deleteNotes(note)
    }

}