package id.dtprsty.chatttoy.main.view

import id.dtprsty.chatttoy.data.Message

interface IChat {
    fun onMessageComing(chat: Message?)
    fun onMessageUpdate(position: Int, chat: Message?)
    fun onMessageDeleted(position: Int)
}