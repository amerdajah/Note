package com.amerdev.note.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amerdev.note.models.Note

@Database(version = 1, entities = [Note::class])
abstract class NoteDatabase: RoomDatabase() {

    companion object {

        val DATABASE_NAME = "notes_db"

        @Volatile private var instance: NoteDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    abstract fun getNoteDao(): NoteDao?
}