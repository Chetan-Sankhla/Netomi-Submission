package com.example.netomitest.data.model

import java.util.UUID

/**
 * Model representing a message in a chat room.
 */
data class Message(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSent: Boolean = false, // Sent = true, Queued = false
    val isSentByUser: Boolean = false, // By User = true, By Server = false
    val isUnread: Boolean = true, // For P1 feature
    val roomName: String = "Room_01"
)
