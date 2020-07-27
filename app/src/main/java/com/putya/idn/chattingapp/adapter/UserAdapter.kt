package com.putya.idn.chattingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.putya.idn.chattingapp.R
import com.putya.idn.chattingapp.model.Users
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(mContext: Context, mUsers: List<Users>, isChatCheck: Boolean) :
    RecyclerView.Adapter<UserAdapter.ViewHolder?>() {

    private val mContext: Context
    private val mUsers: List<Users>
    private val isChatCheck: Boolean

    var lastMessage: String = ""

    init {
        this.mUsers = mUsers
        this.mContext = mContext
        this.isChatCheck = isChatCheck
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.user_search_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user: Users = mUsers[position]
        holder.userName.text = user!!.getUserName()
        Picasso.get().load(user.getProfile()).placeholder(R.drawable.profile).into(holder.profile)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userName: TextView
        var profile: CircleImageView
        var onlineStatus: CircleImageView
        var offlineStatus: CircleImageView
        var lastMessage: TextView

        init {
            userName = itemView.findViewById(R.id.username)
            profile = itemView.findViewById(R.id.profile_image)
            onlineStatus = itemView.findViewById(R.id.image_online)
            offlineStatus = itemView.findViewById(R.id.image_offline)
            lastMessage = itemView.findViewById(R.id.message_last)
        }
    }
}