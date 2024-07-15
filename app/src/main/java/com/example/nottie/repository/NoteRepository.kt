package com.example.nottie.repository

import com.example.nottie.db.NoteDatabase
import com.example.nottie.model.Note

class NoteRepository (private val db : NoteDatabase){

    suspend fun insertNote(note:Note) = db.getNoteDao().insertNote(note)
    suspend fun editNote(note:Note) = db.getNoteDao().updateNote(note)
    suspend fun deleteNote(note: Note) = db.getNoteDao().deleteNote(note)
    fun getAllNotes() = db.getNoteDao().getAllNotes()
    suspend fun searchNote(query:String?) = db.getNoteDao().searchNote(query)
}