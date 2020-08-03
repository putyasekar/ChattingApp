package com.putya.idn.chattingapp.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.putya.idn.chattingapp.R
import com.putya.idn.chattingapp.model.Users
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_message_chat.*

class MessageChatActivity : AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var userIDVisit: String = ""
    var notify = false

    lateinit var recyclerViewChat: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)

        //toolbar!!!!
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_message_chat)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener { finish() }

        intent = intent
        userIDVisit = intent.getStringExtra("visit_id")
        firebaseUser = FirebaseAuth.getInstance().currentUser

        //chat list!!!!
        recyclerViewChat = findViewById(R.id.recycler_view_chats)
        recyclerViewChat.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        recyclerViewChat.layoutManager = linearLayoutManager

        reference = FirebaseDatabase.getInstance().reference.child("Users").child(userIDVisit)

        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val user: Users? = p0.getValue(Users::class.java)
                username_mchat.text = user!!.getUserName()
                Picasso.get().load(user.getProfile()).into(profile_image_mchat)

                retrieveMessage(firebaseUser!!.uid, userIDVisit, user.getProfile())
            }
        })

        attct_image_file_btn.setOnClickListener {
            notify = true
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(
                Intent.createChooser(intent, getString(R.string.pick_image)),
                438
            )
        }

        seenMessage(userIDVisit)

        iv_send_message.setOnClickListener {
            val message = et_message.text.toString()
            if (message == "") {
                Toast.makeText(this, getString(R.string.write_message_first), Toast.LENGTH_LONG)
                    .show()
            } else {
                sendMessageToUser(firebaseUser!!.uid, userIDVisit, message)
            }
        }
    }

    private fun seenMessage(userIDVisit: String?) {

    }

    private fun retrieveMessage(uid: String, userIDVisit: String?, profile: String?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 438 && resultCode == Activity.RESULT_OK && data != null && data!!.data != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage(getString(R.string.image_upload))
            progressDialog.show()

            val fileUri = data.data
            val storageReference = FirebaseStorage.getInstance().reference.child("Chat Images")
            val ref = FirebaseDatabase.getInstance().reference
            val messageID = ref.push().key
            val filePath = storageReference.child("$messageID.jpg")
            var uploadTask: StorageTask<*>
            uploadTask = filePath.putFile(fileUri!!)
        }
    }

    private fun sendMessageToUser(senderID: String, receiverID: String, message: String) {
        val reference = FirebaseDatabase.getInstance().reference
        val messageKey = reference.push().key

        val messageHashMap = HashMap<String, Any?>()
        messageHashMap["sender"] = senderID
        messageHashMap["receiver"] = receiverID
        messageHashMap["message"] = message
        messageHashMap["iSeen"] = false
        messageHashMap["url"] = ""
        messageHashMap["messageID"] = messageKey

        reference.child("Chats").child(messageKey!!)
            .setValue(messageHashMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val chatListReference = FirebaseDatabase.getInstance()
                        .reference.child("ChatList").child(firebaseUser!!.uid)
                        .child(userIDVisit)

                    chatListReference.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (!p0.exists()) {
                                chatListReference.child("id").setValue(userIDVisit)
                                val chatListReceiverReference =
                                    FirebaseDatabase.getInstance().reference.child("ChatList")
                                        .child(userIDVisit)
                                        .child(firebaseUser!!.uid)

                                chatListReceiverReference.child("id").setValue(firebaseUser!!.uid)
                            }

                        }
                    })
                }
            }

        //push notif!!!!
        val userReference =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(Users::class.java)
                if (notify) {
                    sendNotification(receiverID, user!!.getUserName(), message)
                }
            }
        })
    }

    private fun sendNotification(receiverID: String, userName: String?, message: String) {

    }
}