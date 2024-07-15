package com.example.nottie.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nottie.R
import com.example.nottie.databinding.FragmentEditFragmentBinding
import com.example.nottie.model.Note
import com.example.nottie.ui.MainActivity
import com.example.nottie.viewmodel.NoteViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditFragment : Fragment(R.layout.fragment_edit_fragment) {
    private var _binding :FragmentEditFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesViewModel: NoteViewModel
    private lateinit var currentNote : Note

    private val args : EditFragmentArgs by navArgs()
    val TAG = "jidjiw"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewModel = (activity as MainActivity).noteViewModel
        currentNote = args.note!!

        binding.addNotetitle.setText(currentNote.noteTitle)
        binding.addNoteDesc.setText(currentNote.noteDesc)

//        binding.editNoteFab.setOnClickListener {
//            val noteTitle = binding.editNotetitle.text.toString().trim()
//            val noteDesc = binding.editNoteDesc.text.toString().trim()
//
//            if(noteTitle.isNotEmpty()){
//                val note = Note(currentNote.id,noteTitle,noteDesc)
//                notesViewModel.editNote(note)
//                Toast.makeText(context,"Note Edited",Toast.LENGTH_LONG).show()
//                Log.d("efe", "onViewCreated: "+note.toString())
//            }else{
//                Toast.makeText(context,"Please enter note title", Toast.LENGTH_LONG).show()
//            }
//        }
        binding.addNoteDesc.addTextChangedListener {
            val noteTitle = binding.addNotetitle.text.toString().trim()
            val noteDesc = binding.addNoteDesc.text.toString().trim()

            if(noteTitle.isNotEmpty()){
                val note = Note(currentNote.id,noteTitle,noteDesc)
                notesViewModel.editNote(note)
                Toast.makeText(context,"Note Edited",Toast.LENGTH_LONG).show()
                Log.d("efe", "onViewCreated: "+note.toString())
            }else{
                Toast.makeText(context,"Please enter note title", Toast.LENGTH_LONG).show()
            }
            binding.edittimeTV.apply {
                visibility = View.VISIBLE
                val currentDateTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("hh:mm a")
                val formattedTime = currentDateTime.format(formatter)
                text = "Edited On $formattedTime"
            }


        }

        binding.deleteIcon.setOnClickListener {
            deleteNote()

        }

        notesViewModel.adeNotes?.observe(
            viewLifecycleOwner
        ) { operationResult ->
            operationResult?.let { result ->
                if (result["Edit"] == true) {
                    notesViewModel.clearAdeNotes()
                } else if (result["Delete"] == true) {
                    notesViewModel.clearAdeNotes() // Clear the value instead of setting it to null
                    findNavController().navigate(R.id.action_editFragment_to_mainFragment)
                } else {
                    Toast.makeText(context, "Failed, please retry!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.backIcon.setOnClickListener {
            findNavController().navigate(R.id.action_editFragment_to_mainFragment)
        }
    }

    private fun deleteNote(){
        AlertDialog.Builder(activity).apply{
            setTitle("Delete Note")
            setMessage("Do you want to delete this note?")
            setPositiveButton("Delete"){_,_ ->
                notesViewModel.deleteNote(currentNote)
                findNavController().popBackStack()
                Toast.makeText(context,"Note Deleted",Toast.LENGTH_LONG).show()

            }
            setNegativeButton("Cancel",null)
        }.create().show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}