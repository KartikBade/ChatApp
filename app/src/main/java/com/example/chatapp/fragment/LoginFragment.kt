package com.example.chatapp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.activity.ChatActivity
import com.example.chatapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater)
        mAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }

        binding.loginButton.setOnClickListener {

            try {
                val email = binding.emailEdittext.text.toString().trim()
                val password = binding.passwordEdittext.text.toString().trim()

                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(context as Activity) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Logged In Successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, ChatActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(context, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                            Log.e("LoginFragment", task.exception.toString())
                        }
                    }
            } catch (e: java.lang.Exception) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("LoginFragment", e.toString())
            }
        }
    }
}