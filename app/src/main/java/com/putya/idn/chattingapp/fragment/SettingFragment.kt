package com.putya.idn.chattingapp.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.putya.idn.chattingapp.R
import com.putya.idn.chattingapp.model.Users
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_setting.view.*


class SettingFragment : Fragment() {
    var userReference: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null
    private val RequestCode = 438
    private val imageUri: Uri? = null
    private var storageRef: StorageReference? = null
    private var coverCheck: String? = ""
    private var socialMedia: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        userReference =
            FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")

        userReference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user: Users? = p0.getValue(Users::class.java)

                    if (context != null) {
                        view.tv_username_settings.text = user!!.getUserName()
                        Picasso.get().load(user.getProfile()).into(view.iv_profile_setting)
                        Picasso.get().load(user.getCover()).into(view.iv_cover)
                    }
                }
            }

        })

        return view
    }

}