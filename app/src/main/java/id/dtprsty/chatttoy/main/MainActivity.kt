package id.dtprsty.chatttoy.main

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.dtprsty.chatttoy.R
import id.dtprsty.chatttoy.data.Message
import id.dtprsty.chatttoy.data.UserInfo
import id.dtprsty.chatttoy.login.LoginActivity
import id.dtprsty.chatttoy.main.adapter.ChatAdapter
import id.dtprsty.chatttoy.main.view.IChat
import id.dtprsty.chatttoy.session.UserSession
import id.dtprsty.chatttoy.util.Session
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import java.util.*

class MainActivity : AppCompatActivity(), IChat {

    private val TAG = "main"
    private var user: UserInfo? = null

    private lateinit var viewModel: MainViewModel

    private var listChat: MutableList<Message> = ArrayList()

    private lateinit var adapterChat: ChatAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        action()
    }

    private fun init(){
        toolbar.title = "ChatToy"
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
        user = UserSession.initPreferences(this).userInfo
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        showLoading(true)

        initRecyclerView()
        viewModel.getMessage(this)
    }

    private fun initRecyclerView(){
        adapterChat = ChatAdapter(
            this,
            listChat
        )
        with(rvChat) {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context).apply { stackFromEnd = true }
            this.adapter = adapterChat
        }
    }

    private fun action(){
        btnSend.setOnClickListener {
            val message = etMessage?.text.toString()
            val time = Session.time
            val chat = Message()
            chat.userId = user?.userId
            chat.message = message
            chat.date = time

            viewModel.sendMessage(chat)

            etMessage?.setText("")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId ?: 0 == R.id.menu_logout) {
            UserSession.initPreferences(this).clear()
            startActivity<LoginActivity>()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMessageComing(chat: Message?) {
        listChat.add(chat!!)
        adapterChat.notifyItemInserted(listChat.lastIndex)

        if(chat.userId != user?.userId){
            viewModel.getUser(chat.userId!!)
            viewModel.userInfo.observe(this, Observer {
                if(it != null){
                    adapterChat.setSender(it)
                }
                showLoading(false)
            })
        }
    }

    override fun onMessageUpdate(position: Int, chat: Message?) {
        listChat[position] = chat!!
        adapterChat.notifyItemChanged(position, chat)
    }

    override fun onMessageDeleted(position: Int) {
        listChat.removeAt(position)
        adapterChat.notifyItemRemoved(position)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}
