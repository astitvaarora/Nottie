package com.example.nottie.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.nottie.R
import com.example.nottie.adapter.NoteAdapter
import com.example.nottie.databinding.FragmentMainBinding
import com.example.nottie.model.Note
import com.example.nottie.ui.MainActivity
import com.example.nottie.viewmodel.NoteViewModel
import com.google.firebase.auth.FirebaseAuth

class MainFragment : Fragment(R.layout.fragment_main),NoteAdapter.OnNoteClickListener {
    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var notesViewModel : NoteViewModel? = null
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(FirebaseAuth.getInstance().currentUser!=null) {
                    activity?.finish()
                }else{
                    findNavController().popBackStack()
                }
                Toast.makeText(requireContext(), "Back Pressed", Toast.LENGTH_SHORT).show()
            }
        })
        binding.btnMenu.setOnClickListener {

            val contextThemeWrapper = ContextThemeWrapper(requireContext(), R.style.CustomStyle)
            val popupMenu = PopupMenu(contextThemeWrapper, binding.btnMenu)
            val inflater: MenuInflater = popupMenu.menuInflater
            inflater.inflate(R.menu.menu, popupMenu.menu)


            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.logout -> {
                        FirebaseAuth.getInstance().signOut()
                        findNavController().navigate(R.id.signinFragment, null, navOptions {
                            popUpTo(R.id.mainFragment) { inclusive = true }
                        })
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        notesViewModel = (activity as? MainActivity?)?.noteViewModel

        if(notesViewModel==null){
            Log.d("nien", "onViewCreated: viewModelIsNUll")

        }
        setupHomeRecyclerView()

        notesViewModel?.notes?.observe(viewLifecycleOwner) { notes ->
            notes?.let {
                noteAdapter.differ.submitList(it)
                updateUI(it)
            }?:Log.d("nien", "onViewCreated: NotesIsNUll")
        }

        binding.addNoteFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_addFragment)
        }
        noteAdapter.setOnItemClickListener { note ->
            val direction = MainFragmentDirections.actionMainFragmentToEditFragment(note as Note?)
            findNavController().navigate(direction)
        }
        binding.searchBar.addTextChangedListener(
            object:TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    Log.d("textchanged", "onTextChanged: $p0")
                    searchNote(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            }
        )

    }
    override fun onNoteClick(note: Note) {
        val direction = MainFragmentDirections.actionMainFragmentToEditFragment(note)
        findNavController().navigate(direction)
    }
    private fun setupHomeRecyclerView(){
        //notesViewModel.getAllNotes()
        noteAdapter = NoteAdapter(this)
        binding.homeRecyclerView.apply{
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            adapter = noteAdapter
        }
        activity?.let {
            notesViewModel?.notes?.observe(viewLifecycleOwner){ note->
                Log.d("jow", "setupHomeRecyclerView: "+note.size.toString())
                noteAdapter.differ.submitList(note)
                updateUI(note)
            }
        }
        Log.d("Astitva",noteAdapter.differ.currentList.toString())
    }


    override fun onResume() {
        super.onResume()
        notesViewModel?.getAllNotes()
        Log.d("niwfn", "onResume: true ")
    }
    private fun updateUI(notes: List<Note>?) {
        if (notes.isNullOrEmpty()) {
            binding.homeRecyclerView.visibility = View.GONE
            binding.emptydescTV.visibility = View.VISIBLE
        } else {
            binding.homeRecyclerView.visibility = View.VISIBLE
            binding.emptydescTV.visibility = View.GONE
        }
    }
    private fun searchNote(query:String?){
        val searchQuery = "%$query"
        val ls = notesViewModel?.searchNote(searchQuery){
            noteAdapter.differ.submitList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}