package id.dtprsty.chatttoy.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.muddzdev.styleabletoast.StyleableToast
import id.dtprsty.chatttoy.R
import id.dtprsty.chatttoy.main.MainActivity
import id.dtprsty.chatttoy.session.UserSession
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        checkLoginState()

        btnLogin.setOnClickListener {
            showLoading(true)
            viewModel.validation(this, tvEmail.text.toString(), tvPassword.text.toString())
        }
        getErrorMessage()
        isLoginSuccess()
    }

    private fun isLoginSuccess() {
        viewModel.getLoginStatus().observe(this, Observer {
            if (it) {
                goToMainActivity()
            }
            showLoading(false)
        })
    }

    private fun getErrorMessage() {
        viewModel.getErrorMessage().observe(this, Observer {
            error(it)
        })
    }

    private fun error(stringResource: Int) {
        StyleableToast.makeText(
            this,
            getString(stringResource),
            Toast.LENGTH_SHORT,
            R.style.error_toast
        ).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
            root.alpha = 0.2f
            btnLogin.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            root.alpha = 1.0f
            btnLogin.visibility = View.VISIBLE
        }
    }

    private fun checkLoginState(){
        if(!UserSession.initPreferences(this).userInfo?.userId.isNullOrEmpty()){
            goToMainActivity()
        }
    }

    private fun goToMainActivity(){
        startActivity<MainActivity>()
        finish()
    }
}
