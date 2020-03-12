package id.dtprsty.chatttoy.main.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import id.dtprsty.chatttoy.R
import id.dtprsty.chatttoy.data.Message
import id.dtprsty.chatttoy.data.UserInfo
import kotlinx.android.synthetic.main.from_message.view.*

class HolderMessageFrom(itemView: View) : RecyclerView.ViewHolder(itemView){

    fun bindChatContent(chat: Message, user: UserInfo) {
        with(itemView){
            Glide.with(context)
                .load(user?.avatar)
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_sentiment_very_satisfied_black_24dp)
                )
                .into(ivAvatar)
            tvUsername.text = user?.name
            tvMessage.text = chat.message
            tvDate.text = chat.date
        }
    }

}