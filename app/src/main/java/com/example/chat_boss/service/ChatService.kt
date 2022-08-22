package com.example.chat_boss.service

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import im.threads.ChatStyle
import im.threads.ConfigBuilder
import im.threads.ThreadsLib
import im.threads.UserInfoBuilder
import im.threads.view.ChatActivity
import org.json.JSONObject
import javax.inject.Inject

class ChatService @Inject constructor(@ApplicationContext private val context: Context) {

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    fun runChatActivity(context: Context) {
        context.startActivity(Intent(context, ChatActivity::class.java))
    }

    private fun provideClientData(): JSONObject {
        val metaData = JSONObject()
        val coordinates = JSONObject().put("coordinates", "$latitude, $longitude")

        metaData.apply {
            put("meta_data", coordinates)
            put("CLIENT_MESSAGES_TYPE", "audio")
        }
        return metaData
    }

    fun setGeoPosition(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude

        Log.d("QQQ", "$latitude + $longitude")

        reInitialize()
    }

    private fun reInitialize() {
        val clientId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        val userInfoBuilder = UserInfoBuilder(clientId)
        userInfoBuilder.setClientData(provideClientData().toString())

        with(ThreadsLib.getInstance()) {
            logoutClient(clientId)
            initUser(userInfoBuilder)
        }
    }

    fun initChatBot() {
        val config = ConfigBuilder(context)
            .surveyCompletionDelay(2000)
            .historyLoadingCount(50)
            .isDebugLoggingEnabled(true)
        ThreadsLib.init(config)

        val style = ChatStyle()
            .setVoiceMessageEnabled(true)
            .setUseExternalCameraApp(true)
            .setInputEnabledDuringQuickReplies(true)

        ThreadsLib.getInstance().applyChatStyle(style)

        val clientId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val userInfoBuilder = UserInfoBuilder(clientId).setClientData(provideClientData().toString())

        ThreadsLib.getInstance().initUser(userInfoBuilder)
    }

}