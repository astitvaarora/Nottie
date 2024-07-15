package com.example.nottie.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nottie.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note:Note)

    @Delete
    suspend fun deleteNote(note:Note)

    @Query("SELECT * FROM NOTES ORDER BY ID DESC")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM NOTES WHERE noteTitle LIKE '%' || :query || '%' OR noteDesc LIKE '%' || :query || '%'")
     suspend fun searchNote(query: String?): List<Note>

}