package com.example.netomitest.data.socket

import android.util.Log
import com.example.netomitest.data.model.Message
import com.google.gson.Gson
import com.piesocket.channels.BuildConfig
import com.piesocket.channels.Channel
import com.piesocket.channels.PieSocket
import com.piesocket.channels.misc.PieSocketEvent
import com.piesocket.channels.misc.PieSocketEventListener
import com.piesocket.channels.misc.PieSocketOptions
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor() {

    private var pieSocket: PieSocket? = null
    private var channel: Channel? = null
    private val roomId01: String = "Room_01"
    private val roomId02: String = "Room_02"
    private val roomId03: String = "Room_03"


    private val _messageFlow = MutableSharedFlow<Message>(replay = 0, extraBufferCapacity = 1)
    val messageFlow = _messageFlow.asSharedFlow()

    fun connect() {
        val options = PieSocketOptions()
        options.clusterId = BuildConfig.CLUSTER_ID
        options.apiKey = BuildConfig.API_KEY

        pieSocket = PieSocket(options)
        channel = pieSocket?.join(roomId01)
        //channel = pieSocket?.join(roomId02)
        //channel = pieSocket?.join(roomId03)

        channel?.let { listen(it, "system:connected") }
        channel?.let { listen(it, "new-message") }


    }

    private fun listen(channel: Channel, eventName: String) {
        // Listen to messages
        channel.listen(eventName, object : PieSocketEventListener() {
            override fun handleEvent(event: PieSocketEvent?) {
                event?.data?.let { message ->
                    if (event.event == "new-message") {
                        // Parsing the meta-data to retrieve user info and timestamp
                        val metaData: Message? = Gson().fromJson(event.meta, Message::class.java)
                        metaData?.let {
                            val messageId = metaData.id
                            val dateTime = metaData.timestamp
                            val userId = "1"
                            val userName = "User1"
                            emitMessage(message, dateTime, userId, userName, messageId)
                        } ?: run {
                            emitMessage(
                                content = message,
                                dateTime = System.currentTimeMillis(),
                                userId = "0",
                                userName = "Server",
                                messageId = ""
                            )
                        }
                    }
                }
            }
        })
    }

    fun send(eventName: String, message: Message) {
        val event = PieSocketEvent(eventName)
        event.data = message.text

        // Convert metadata map to JSON string (for better structure)
        event.meta = Gson().toJson(message)
        channel?.publish(event)
    }

    private fun emitMessage(
        content: String,
        dateTime: Long,
        userId: String,
        userName: String,
        messageId: String
    ) {
        val message = if (userId == "1" && userName == "User1") {
            Message(
                id = messageId,
                text = content,
                timestamp = dateTime,
                isSent = true,
                isSentByUser = true,
                isUnread = false
            )

        } else {
            Message(
                text = content,
                timestamp = dateTime,
                isSent = true,
                isSentByUser = false,
                isUnread = true
            )
        }
        _messageFlow.tryEmit(message)
    }

    fun disconnect() {
        channel?.disconnect()
    }
}