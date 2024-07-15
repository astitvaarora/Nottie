package com.example.nottie.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.nottie.R
import com.example.nottie.databinding.FragmentMainBinding
import com.example.nottie.databinding.FragmentSignupBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class SignupFragment : Fragment() {
    private var _binding : FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val RC_SIGN_IN = 9001
    private val webClientId: String = "844647513129-v0lf23v17a2p1o6ohvf7er9hevhbk4ac.apps.googleusercontent.com"
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater,container,false)
        FirebaseApp.initializeApp(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        binding.btnGoogleLogin.setOnClickListener {
            signIn()
            //findNavController().navigate(R.id.action_signupFragment_to_mainFragment)
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_signinFragment)

        }
        binding.btnGetStarted.setOnClickListener {
            val email = binding.edittextEmail.text.toString()
            val password = binding.edittextPassword.text.toString()
            val confirmPassword = binding.edittextConfirmPassword.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                registerUser(email,password,confirmPassword)
            }else{
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }

            //findNavController().navigate(R.id.action_signupFragment_to_signinFragment)

        }
    }
    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    fun registerUser(email: String, password: String, currPass:String) {
        if(password.equals(currPass)){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Registration successful, handle the new user
                        val user = FirebaseAuth.getInstance().currentUser
                        val displayName = user?.email ?: "Unknown User"
                        Toast.makeText(requireContext(), "Registered as $displayName", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_signupFragment_to_signinFragment)
                    } else {
                        // Registration failed, handle the error
                        Toast.makeText(requireContext(), "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
            Toast.makeText(requireContext(), "Password Not Matched Retry!", Toast.LENGTH_SHORT).show()
        }

    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        saveUserCredentials(it.email ?: "", it.uid)
                        Toast.makeText(requireContext(), "Google sign in successful", Toast.LENGTH_SHORT).show()
                        navigateToMainFragment()

                    }
                } else {
                    Toast.makeText(requireContext(), "Google sign in failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun navigateToMainFragment() {
        findNavController().navigate(R.id.action_signupFragment_to_mainFragment, null, navOptions {
            popUpTo(R.id.signupFragment) { inclusive = true }
        })
    }

    private fun saveUserCredentials(email: String, uid: String) {
        sharedPreferences.edit().apply {
            putString("email", email)
            putString("uid", uid)
            apply()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}