package com.example.chatapp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.activity.ChatActivity
import com.example.chatapp.databinding.FragmentSignupBinding
import com.example.chatapp.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater)
        mAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        binding.signupButton.setOnClickListener {

            try {
                val name = binding.usernameEdittext.text.toString().trim()
                val email = binding.emailEdittext.text.toString().trim()
                val password = binding.passwordEdittext.text.toString().trim()

                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(context as Activity) { task ->
                        if (task.isSuccessful) {
                            addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                            Toast.makeText(context, "Signed In Successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, ChatActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(context, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                            Log.e("SignupFragment", task.exception.toString())
                        }
                    }
            } catch (e: java.lang.Exception) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("SignupFragment", e.toString())
            }
        }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDatabaseRef = FirebaseDatabase.getInstance().reference

        mDatabaseRef.child("user").child(uid).setValue(User(name, email, uid))
    }
}