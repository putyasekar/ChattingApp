package com.putya.idn.chattingapp.fragment

import android.os.Bundle
import android.os.UserHandle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.putya.idn.chattingapp.R
import com.putya.idn.chattingapp.adapter.UserAdapter
import com.putya.idn.chattingapp.model.Users
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {
    private var userAdapter: UserAdapter? = null
    private var mUser: List<Users>? = null
    private var recyclerView: RecyclerView? = null
    private var searchEdit: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        searchEdit = view.findViewById(R.id.searchUserset)
        recyclerView = view.findViewById(R.id.searchList)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)

        mUser = ArrayList()
        retrieveAllUser()

        searchEdit!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchForUser(s.toString().toLowerCase())
            }
        })

        return view
    }

    private fun searchForUser(toLowerCase: String) {
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val queryUsers =
            FirebaseDatabase.getInstance().reference.child("Users").orderByChild("search")
                .startAt(toLowerCase).endAt(toLowerCase + "\uf8ff")

        queryUsers.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                
            }

            override fun onDataChange(p0: DataSnapshot) {
                (mUser as ArrayList<Users>).clear()

                for (snapshot in p0.children) {
                    val user: Users? = snapshot.getValue(Users::class.java)
                    if (!(user!!.getUID()).equals(firebaseUserID)) {
                        (mUser as ArrayList<Users>).add(user)
                    }
                }
                userAdapter = UserAdapter(context!!, mUser!!, isChatCheck = false)
                recyclerView!!.adapter = userAdapter
            }
        })
    }

    private fun retrieveAllUser() {
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val refUsers = FirebaseDatabase.getInstance().reference.child("Users")
        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                (mUser as ArrayList<Users>).clear()
                if (searchEdit!!.text.toString() == "") {
                    for (snapshot in p0.children) {
                        val user: Users? = snapshot.getValue(Users::class.java)
                        if (!(user!!.getUID()).equals(firebaseUserID)) {
                            (mUser as ArrayList<Users>).add(user)
                        }
                    }
                    userAdapter = UserAdapter(context!!, mUser!!, false)
                    recyclerView!!.adapter = userAdapter
                }
            }
        })
    }
}