package com.example.chat_boss

import android.app.Application
import com.example.chat_boss.service.ChatService
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject lateinit var chatService: ChatService

    override fun onCreate() {
        super.onCreate()
        chatService.initChatBot()
    }

}