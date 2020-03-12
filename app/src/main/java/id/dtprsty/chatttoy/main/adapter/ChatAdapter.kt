package id.dtprsty.chatttoy.main.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dtprsty.chatttoy.R
import id.dtprsty.chatttoy.data.Message
import id.dtprsty.chatttoy.data.UserInfo
import id.dtprsty.chatttoy.session.UserSession

class ChatAdapter(context: Context, private val listChat: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var sender: UserInfo? = null

    companion object {
        const val FROM = 1
        const val TO = 2
    }

    val user = UserSession.initPreferences(context).userInfo?.userId
    val userInfo = UserSession.initPreferences(context).userInfo

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FROM -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.from_message, parent, false)
                HolderMessageFrom(v)
            }

            TO -> {
                val v =
                    LayoutInflater.from(parent.context).inflate(R.layout.to_message, parent, false)
                HolderMessageTo(v)
            }

            else -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.from_message, parent, false)
                HolderMessageFrom(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("adapter", "$listChat")
        when (holder) {
            is HolderMessageTo -> holder.bindChatContent(listChat[position], userInfo!!)
            is HolderMessageFrom ->
                if(sender != null) {
                    holder.bindChatContent(listChat[position], sender!!)
                }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (listChat[position].userId == user) TO else FROM
    }

    override fun getItemCount(): Int {
        return listChat.size
    }

    fun setSender(userInfo: UserInfo?){
        this.sender = userInfo
        notifyDataSetChanged()
    }
}