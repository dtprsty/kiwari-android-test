package id.dtprsty.chatttoy.util

import com.google.firebase.database.FirebaseDatabase

object FirebasePath {
    fun chatReferences() = FirebaseDatabase.getInstance().getReference("message")
    fun userReferences() = FirebaseDatabase.getInstance().getReference("users")
}