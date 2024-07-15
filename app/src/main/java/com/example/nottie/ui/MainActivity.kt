package com.example.nottie.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.nottie.R
import com.example.nottie.adapter.NoteAdapter
import com.example.nottie.databinding.ActivityMainBinding
import com.example.nottie.db.NoteDatabase
import com.example.nottie.repository.NoteRepository
import com.example.nottie.viewmodel.NoteViewModel
import com.example.nottie.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    lateinit var noteViewModel : NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment = supportFragmentManager.findFragmentById(binding.FragmentCV.id) as NavHostFragment
        //val navController = navHostFragment.navController
        setupViewModel()

    }
    private fun setupViewModel(){
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application,noteRepository)
        noteViewModel = ViewModelProvider(this,viewModelProviderFactory)[NoteViewModel::class.java]
    }
}