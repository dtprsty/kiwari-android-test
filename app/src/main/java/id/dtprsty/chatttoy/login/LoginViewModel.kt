package id.dtprsty.chatttoy.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import id.dtprsty.chatttoy.R
import id.dtprsty.chatttoy.data.UserInfo
import id.dtprsty.chatttoy.session.UserSession
import id.dtprsty.chatttoy.util.FirebasePath


class LoginViewModel : ViewModel() {

    private val TAG = "LoginVM"

    val errorMessage = MutableLiveData<Int>()
    val isLoginSuccess = MutableLiveData<Boolean>()

    val database = Firebase.database.reference

    fun validation(context: Context, email: String, password: String){
        val query = FirebasePath.userReferences().orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for (i in dataSnapshot.children) {
                        val user = i.getValue(UserInfo::class.java)

                        if(user?.password.equals(password)){
                            isLoginSuccess.postValue(true)
                            UserSession.initPreferences(context).userInfo = user
                        }else{
                            isLoginSuccess.postValue(false)
                            errorMessage.postValue(R.string.login_failed)
                        }
                    }
                }else{
                    isLoginSuccess.postValue(false)
                    errorMessage.postValue(R.string.email_not_exist)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
    }

    internal fun getErrorMessage(): LiveData<Int> {
        return errorMessage
    }

    internal fun getLoginStatus(): LiveData<Boolean> {
        return isLoginSuccess
    }
}