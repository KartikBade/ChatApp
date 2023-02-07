package com.example.chatapp.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

class ChatActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            try {
                mAuth.signOut()
                Toast.makeText(applicationContext, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ChatActivity, AuthActivity::class.java)
                finish()
                startActivity(intent)
            } catch (e: java.lang.Exception) {
                Toast.makeText(this@ChatActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("ChatActivity", e.toString())
            }
            return true
        }
        return true
    }
}