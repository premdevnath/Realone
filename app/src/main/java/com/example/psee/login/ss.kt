package com.example.psee.login
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.psee.R

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
 // For accessing R.drawable.logo (auto-generated by Android Studio)

// Your SplashScreen function
@Composable
fun SplashScreen(navController: NavHostController) {
    // Add delay for splash screen duration
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    val currentUser = auth.currentUser
    LaunchedEffect(Unit) {
        delay(1000)
       // onTimeout()
        if (currentUser != null) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    // UI for Splash Screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Background color
        contentAlignment = Alignment.Center
    ) {
        // Image Composable for Logo
        Image(

            painter = painterResource(id = R.drawable.home), // Replace 'logo' with your actual drawable name
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp) // Customize size
        )
    }
}