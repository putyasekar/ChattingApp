package com.putya.idn.chattingapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.putya.idn.chattingapp.MainActivity
import com.putya.idn.chattingapp.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignupActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val toolBar: Toolbar = findViewById(R.id.toolbar_signup)
        setSupportActionBar(toolBar)

        supportActionBar!!.title = getString(R.string.text_signup)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolBar.setNavigationOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        mAuth = FirebaseAuth.getInstance()
        btn_signup.setOnClickListener {
            signupUser()
        }
    }

    private fun signupUser() {
        val username: String = et_user_name_signup.text.toString()
        val email: String = et_email.text.toString()
        val password: String = et_password.text.toString()

        if (username == "") {
            Toast.makeText(this, getString(R.string.text_message_username), Toast.LENGTH_LONG)
                .show()

        } else if (email == "") {
            Toast.makeText(this, getString(R.string.text_message_email), Toast.LENGTH_LONG).show()

        } else if (password == "") {
            Toast.makeText(this, getString(R.string.text_message_password), Toast.LENGTH_LONG)
                .show()

        } else {

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseUserID = mAuth.currentUser!!.uid
                    refUsers = FirebaseDatabase.getInstance()
                        .reference.child(getString(R.string.text_user)).child(firebaseUserID)

                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = firebaseUserID
                    userHashMap["username"] = username
                    userHashMap["profile"] =
                        "https://firebasestorage.googleapis.com/v0/b/chattingapp-9f61f.appspot.com/o/profile.png?alt=media&token=20b2a932-5e54-4110-8dd5-f32e0e3c6784"
                    userHashMap["cover"] =
                        "https://firebasestorage.googleapis.com/v0/b/chattingapp-9f61f.appspot.com/o/cover.jpeg?alt=media&token=7f8044c8-41d7-499f-b6c2-c7522cc4a872"
                    userHashMap["status"] = "offline"
                    userHashMap["search"] = username.toLowerCase()
                    userHashMap["facebook"] = "https://m.facebook.com"
                    userHashMap["instagram"] = "https://m.instagram.com"
                    userHashMap["website"] = "https://www.google.com"

                    refUsers.updateChildren(userHashMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    Toast.makeText(
                        this, getString(R.string.text_error_message) + task.exception!!
                            .message.toString(), Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}