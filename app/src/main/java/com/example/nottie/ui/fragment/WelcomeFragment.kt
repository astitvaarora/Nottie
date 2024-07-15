package com.example.nottie.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.nottie.R
import com.example.nottie.databinding.FragmentWelcomeBinding
import com.google.firebase.auth.FirebaseAuth

class WelcomeFragment : Fragment() {
    private var _binding : FragmentWelcomeBinding? = null
    private val binding  get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(FirebaseAuth.getInstance().currentUser!=null){
            findNavController().navigate(R.id.action_welcomeFragment_to_mainFragment)

        }
        binding.getstartedBtn.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_signupFragment)
        }
        binding.signInTv.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_signinFragment)
        }
    }

}