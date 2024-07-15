package com.example.nottie.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nottie.repository.NoteRepository

class NoteViewModelFactory(private val app: Application, private val noteRepository: NoteRepository) :
    ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(noteRepository) as T
    }
}