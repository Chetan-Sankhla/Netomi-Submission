package com.example.netomitest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.netomitest.ui.chat.ChatDetailScreen
import com.example.netomitest.ui.home.ChatHomeScreen
import com.example.netomitest.ui.home.ChatHomeViewModel
import com.example.netomitest.ui.theme.NetomiTestTheme
import com.example.netomitest.utils.NetworkObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkObserver: NetworkObserver

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        val isConnected = mutableStateOf(true)
        val showMessage = mutableStateOf(false)

        lifecycleScope.launch {
            networkObserver.networkStatus.collect { connected ->
                isConnected.value = connected
                showMessage.value = true

                if (connected) {
                    delay(2000) // Show "Back Online" for 2 seconds
                    showMessage.value = false
                }
            }
        }
        setContent {
            NetomiTestTheme {
                // Set up the NavController for navigation
                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        Column {
                            ConnectivityStatusBar(isConnected.value, showMessage.value)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    // Set up the NavHost to manage navigation between screens
                    NavHost(
                        navController = navController,
                        startDestination = "chat_home", // Default screen
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Home screen route
                        composable("chat_home") { backStackEntry ->
                            val parentEntry = remember(backStackEntry) {
                                navController.getBackStackEntry("chat_home")
                            }
                            val viewModel: ChatHomeViewModel = hiltViewModel(parentEntry)
                            ChatHomeScreen(
                                viewModel,
                                onChatClick = { chatRoom ->
                                    // Navigate to ChatDetailScreen on item click
                                    navController.navigate("chat_details/${chatRoom.name}")
                                }
                            )
                        }

                        // Detail screen route
                        composable(
                            route = "chat_details/{chatRoomName}",
                            arguments = listOf(navArgument("chatRoomName") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val parentEntry = remember(backStackEntry) {
                                navController.getBackStackEntry("chat_home")
                            }
                            val viewModel: ChatHomeViewModel = hiltViewModel(parentEntry)
                            val chatRoomName = backStackEntry.arguments?.getString("chatRoomName")
                            ChatDetailScreen(viewModel, chatRoomName = chatRoomName ?: "", onBackPress = {
                                navController.popBackStack()
                            })
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ConnectivityStatusBar(isConnected: Boolean, showMessage: Boolean) {
        if (showMessage) {
            val backgroundColor = if (isConnected) Color(0xFF4CAF50) else Color(0xFFF44336) // Green or Red
            val message = if (isConnected) "Back Online" else "No Internet Connection"

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}