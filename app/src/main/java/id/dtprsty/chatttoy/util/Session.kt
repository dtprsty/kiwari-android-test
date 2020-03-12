package id.dtprsty.chatttoy.util

import java.text.SimpleDateFormat
import java.util.*

object Session {

    const val PREF_USERNAME = "user"
    const val PREF_USER_ID = "id"
    const val PREF_EMAIL = "email"
    const val PREF_AVATAR = "avatar"

    val time: String
        get() = SimpleDateFormat("dd MMM yyyy , HH.mm", Locale.getDefault()).format(Date())
}