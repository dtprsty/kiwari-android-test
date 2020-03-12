package id.dtprsty.chatttoy.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import id.dtprsty.chatttoy.data.Message
import id.dtprsty.chatttoy.data.UserInfo
import id.dtprsty.chatttoy.main.view.IChat
import id.dtprsty.chatttoy.util.FirebasePath

class MainViewModel : ViewModel() {

    private val TAG = "MainVM"
    private val messageIds = mutableListOf<String>()
    var userInfo = MutableLiveData<UserInfo>()
    fun sendMessage(message: Message){
        val messageId = FirebasePath.chatReferences().push().key
        FirebasePath.chatReferences()
            .child(messageId ?: "0")
            .setValue(message.copy(messageId = messageId))
    }

    fun getMessage(view: IChat){
        FirebasePath.chatReferences()
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                    val chat = dataSnapshot.getValue(Message::class.java)
                    val position = messageIds.indexOf(chat?.messageId)

                    view.onMessageUpdate(position, chat)
                }

                override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                    val chat = dataSnapshot.getValue(Message::class.java)
                    messageIds.add(chat?.messageId ?: "0")

                    view.onMessageComing(chat)
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    val chat = dataSnapshot.getValue(Message::class.java)
                    val position = messageIds.indexOf(chat?.messageId)

                    messageIds.removeAt(position)
                    view.onMessageDeleted(position)
                }
            })
    }

    fun getUser(userId: String){
        val query = FirebasePath.userReferences().orderByChild("userId").equalTo(userId)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for (i in dataSnapshot.children) {
                        val user = i.getValue(UserInfo::class.java)
                        userInfo.postValue(user)
                    }
                }else{
                    Log.d(TAG, "user not found")
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
    }

}