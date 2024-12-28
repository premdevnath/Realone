package com.example.psee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room

import com.example.psee.ui.theme.PseeTheme
import dagger.hilt.android.AndroidEntryPoint


class MainActivity : ComponentActivity() {
  //  private lateinit var db: AppDatabase // Room Database ka reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      
        enableEdgeToEdge()
        var newviewmodel =ViewModelProvider(this)[viewmodle::class.java]
        setContent {

            var navController = rememberNavController()
            PseeTheme {
               navigation(navController,newviewmodel)
            }
        }
    }
}
