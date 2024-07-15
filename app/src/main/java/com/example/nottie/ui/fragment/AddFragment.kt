package com.example.nottie.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.nottie.R
import com.example.nottie.databinding.FragmentAddfragmentBinding
import com.example.nottie.model.Note
import com.example.nottie.ui.MainActivity
import com.example.nottie.viewmodel.NoteViewModel
import java.util.Locale

class AddFragment : Fragment() {
    private var _binding : FragmentAddfragmentBinding? = null
    private val binding  get() = _binding!!
    private lateinit var notesViewModel : NoteViewModel
    private lateinit var addNoteView : View
    private var color : Int = 0
    var TAG = "wdwo"
    private lateinit var textToSpeech: TextToSpeech
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddfragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesViewModel = (activity as MainActivity).noteViewModel
        addNoteView = view

        binding.addIcon.setOnClickListener{
            saveNote(addNoteView)
        }


        notesViewModel.adeNotes?.observe(
            viewLifecycleOwner
        ) {operationResult ->
            operationResult?.let{
                if (it["Add"] == true) {
                    notesViewModel.adeNotes = null
                    //findNavController().navigate(R.id.action_addFragment_to_mainFragment)
                } else {
                    Toast.makeText(addNoteView.context, "Failed pls retry!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result->
            if (result.resultCode == Activity.RESULT_OK){
                val results = result.data?.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
                ) as ArrayList<String>

                binding.addNoteDesc.setText(results[0])
            }
        }
        val speechBtn = binding.speechIcon
        speechBtn.setOnClickListener {
            try {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    Locale.getDefault()
                )
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something")
                result.launch(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

    }

    private fun saveNote(view:View){
        val noteTitle = binding.addNotetitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()

        if(noteTitle.isNotEmpty()){
            val note = Note(0,noteTitle,noteDesc)
            notesViewModel.addNote(note)

            Toast.makeText(addNoteView.context,"Note Saved", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_mainFragment)

        }else{
            Toast.makeText(addNoteView.context,"Please enter note title", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}