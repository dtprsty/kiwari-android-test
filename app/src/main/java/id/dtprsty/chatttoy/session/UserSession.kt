package id.dtprsty.chatttoy.session

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import id.dtprsty.chatttoy.data.UserInfo
import id.dtprsty.chatttoy.util.Session

class UserSession {

    var userInfo: UserInfo?
        get() {
            val user = UserInfo()
            with(user){
                name = preferences.getString(Session.PREF_USERNAME, "")
                email = preferences.getString(Session.PREF_EMAIL, "")
                userId = preferences.getString(Session.PREF_USER_ID, "")
                avatar = preferences.getString(Session.PREF_AVATAR, "")
            }
            return user
        }
        set(modelUser) {
            val editor = preferences.edit()
            editor.putString(Session.PREF_USERNAME, modelUser?.name)
                .putString(Session.PREF_EMAIL, modelUser?.email)
                .putString(Session.PREF_USER_ID, modelUser?.userId)
                .putString(Session.PREF_AVATAR, modelUser?.avatar)
                .apply()
        }

    fun clear() = preferences.edit {
        clear()
        apply()
    }

    companion object {

        private lateinit var preferences: SharedPreferences

        fun initPreferences(context: Context): UserSession {
            preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
            return UserSession()
        }
    }
}