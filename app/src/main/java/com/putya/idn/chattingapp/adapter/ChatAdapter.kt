package com.putya.idn.chattingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.putya.idn.chattingapp.model.Chat
//
//class ChatAdapter(mContext: Context, mChatList: List<Chat>, imageUrl: String) :
//    RecyclerView.Adapter<ChatAdapter.ViewHolder?>() {
//
//    private val mContext: Context
//    private val mChatList: List<Chat>
//    private val imageUrl: String
//    var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
//
//    init {
//        this.mContext = mContext
//        this.mChatList = mChatList
//        this.imageUrl = imageUrl
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
////        return if (viewType == 1) {
////            val view: View = LayoutInflater.from(mContext).inflate()
////        }
//    }
//
//    override fun getItemCount(): Int {
//
//    }
//
//    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {
//
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//    }
//}