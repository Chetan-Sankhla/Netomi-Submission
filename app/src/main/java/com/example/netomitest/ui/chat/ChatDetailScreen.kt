package com.example.netomitest.ui.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.netomitest.data.model.Message
import com.example.netomitest.ui.home.ChatHomeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Screen to show individual chat messages.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    viewModel: ChatHomeViewModel = hiltViewModel(),
    chatRoomName: String,
    onBackPress: () -> Unit
) {
    val messages by viewModel.messages.collectAsState()
    var messageText by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(Unit) {
        viewModel.setActiveRoom(chatRoomName)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.setActiveRoom(null)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(chatRoomName) },
                navigationIcon = {
                    IconButton(onClick = { onBackPress() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            MessageInputField(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSendClick = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage(chatRoomName, messageText)
                        messageText = "" // Clear after sending
                    }
                }
            )
        }
    ) { paddingValues ->
        if (messages.isEmpty()) {
            // Show Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No messages yet. Start the conversation!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                reverseLayout = true
            ) {
                items(messages.reversed()) { message ->
                    ChatMessageItem(message = message)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInputField(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = messageText,
            onValueChange = onMessageChange,
            placeholder = { Text("Type a message...") },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            maxLines = 3
        )
        IconButton(
            onClick = onSendClick
        ) {
            Icon(Icons.Default.Send, contentDescription = "Send")
        }
    }
}

@Composable
fun ChatMessageItem(message: Message) {
    val isSentByUser = message.isSentByUser
    val backgroundColor = if (isSentByUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val alignment = if (isSentByUser) Alignment.End else Alignment.Start
    val textColor = if (isSentByUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    // Formatting the timestamp
    val timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(message.timestamp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (isSentByUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    color = textColor
                )

                // Show timestamp
                Text(
                    text = timestamp,
                    style = MaterialTheme.typography.bodySmall.copy(color = textColor.copy(alpha = 0.6f)),
                    modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
                )
            }
        }

        // Show a "watch icon" if the message is pending (not sent)
        if (!message.isSent) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Pending message",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 4.dp)
                    .size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}




