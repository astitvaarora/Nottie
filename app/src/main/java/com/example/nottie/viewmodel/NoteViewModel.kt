package com.example.nottie.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nottie.model.Note
import com.example.nottie.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel(){
    var notes : LiveData<List<Note>> = MutableLiveData()
    var adeNotes : MutableLiveData<MutableMap<String,Boolean>>? = MutableLiveData<MutableMap<String,Boolean>>()
    var TAG = "wijdowdj2o"
//    fun addNote(note: Note) =
//        viewModelScope.launch {
//            val temp = noteRepository.insertNote(note)
//            val map =
//            if(temp!=null){
//                Log.d(TAG, "addNote: true")
//                hashMapOf(
//                    "Add" to true
//                )
//
//            }else{
//                Log.d(TAG, "addNote: false")
//                hashMapOf(
//                    "Add" to false
//                )
//            }
//            adeNotes?.value = map
//            Log.d(TAG, "addNote: "+adeNotes?.value)
//        }
//    fun editNote(note:Note) =
//        viewModelScope.launch {
//            val temp = noteRepository.editNote(note)
//            Log.d(TAG, "editNote: "+temp.toString())
//            val map =
//                if(temp!=null){
//                    hashMapOf(
//                        "Edit" to true
//                    )
//                }else{
//                    hashMapOf(
//                        "Edit" to false
//                    )
//                }
//            adeNotes?.value = map
//        }
//    fun deleteNote(note:Note) =
//        viewModelScope.launch {
//            val temp = noteRepository.deleteNote(note)
//            val map =
//                if(temp!=null){
//                    hashMapOf(
//                        "Delete" to true
//                    )
//                }else{
//                    hashMapOf(
//                        "Delete" to false
//                    )
//                }
//            adeNotes?.value = map
//        }

    fun addNote(note: Note) = viewModelScope.launch {
        val temp = noteRepository.insertNote(note)
        val map = run {
            Log.d(TAG, "addNote: true")
            hashMapOf("Add" to true)
        }
        adeNotes?.value = map
        Log.d(TAG, "addNote: " + adeNotes?.value)
    }

    fun editNote(note: Note) = viewModelScope.launch {
        val temp = noteRepository.editNote(note)
        Log.d(TAG, "editNote: " + temp.toString())
        val map = hashMapOf("Edit" to true)
        adeNotes?.value = map
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        val temp = noteRepository.deleteNote(note)
        val map = hashMapOf("Delete" to true)
        adeNotes?.value = map
    }

    fun getAllNotes() = viewModelScope.launch(Dispatchers.IO){
        val temp = noteRepository.getAllNotes()
        Log.d(TAG, "getAllNotes: "+temp.toString())
        temp.let {
            (notes as MutableLiveData).postValue(it)
        }
    }
    fun searchNote(query: String?, callback: (List<Note>?) -> Unit) {
        viewModelScope.launch {
            val result = noteRepository.searchNote(query)
            callback(result)
        }
    }
    fun clearAdeNotes() {
        adeNotes?.value = null
    }



}