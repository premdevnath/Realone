package com.example.psee.bottombar.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.psee.EditProfileScreen
import com.example.psee.FullScreenImageScreen


import com.example.psee.R
import com.example.psee.SimplePropertyDetailsScreen

import com.example.psee.chat.chatscreen
import com.example.psee.chatlist

import com.example.psee.dataclass.rvmodel
import com.example.psee.viewmodle
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

//ye app ka mane page hai isme bottom bar haijo 4 screen se dikhegi

//login ke baaad user yaha aayega or use home page show karega

@Composable
fun homepage(navController1: NavHostController, newviewmodel: viewmodle) {
    bottombar()
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun bottombar()
{
    var selected by remember { mutableStateOf("home") }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val navController = rememberNavController()
    var item :rvmodel?=null
    val uid= FirebaseAuth.getInstance().currentUser?.uid ?: ""

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                   // .padding(bottom = 30.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
                    .border(1.dp,Color.Gray)
                    .height(120.dp)

                    .background(Color(0xFF05293C)//, shape = RoundedCornerShape(50))
                    )
                   // .shadow(8.dp, shape = RoundedCornerShape(50)),
                    ,containerColor = Color(0xFFFFFFFF)

            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Home Icon Button
                    IconButton(
                        onClick = {
                            selected = "home"
                            navController.navigate("home_screen")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                              //  .padding(top = 10.dp),
                            ,tint = if (selected == "home") Color(0xFF07527C) else Color.Black
                        )
                    }

                    // Search Icon Button
                    IconButton(
                        onClick = {
                            selected = "search"
                            navController.navigate("search_screen")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                            //  .padding(top = 10.dp),
                            ,tint = if (selected == "search") Color(0xFF07527C) else Color.Black

                        )
                    }

                    //yaha kuch nhi kiya bus ek box banake usme floting button daldiya
                    //or button ke clik pe bottom sheet open hoti hai

                    // Floating Action Button
                    Spacer(modifier = Modifier.width(16.dp)) // Space before FAB
                    IconButton (
                        onClick = { showBottomSheet = true },

                        ) {
                        //agar kahi darble ki image lagani hai to image laga hai
                        Image(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = "Add",
                            modifier = Modifier.size(34.dp)
                                .padding(bottom = 5.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp)) // Add spacing after FAB


                    // Notifications Icon Button
                    IconButton(
                        onClick = {
                            selected = "chat"
                            navController.navigate("notifications_screen")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = if (selected == "chat") Color(0xFF07527C) else Color.Black
                        )
                    }

                    /*
                    IconButton(
                        onClick = {
                            selected = "chat"
                            navController.navigate("chat/{channelId}")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                            //  .padding(top = 10.dp),
                            ,tint = if (selected == "chat") Color(0xFF07527C) else Color.Black
                        )
                    }


                     */

                    // Profile Icon Button
                    IconButton(
                        onClick = {
                            selected = "profile"
                            navController.navigate("profile_screen/$uid")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                            //  .padding(top = 10.dp),
                            ,tint = if (selected == "profile") Color(0xFF07527C) else Color.Black
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "home_screen",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home_screen") { home(navController) }
            composable("notifications_screen") {
                Log.d("Debug21", "Participants:${uid}")
                NotificationScreen(currentUserId = uid, navController = navController)
            }

            composable("search_screen") { search(navController) }
          // composable("profile_screen") { profile(uid,navController) }
            composable("profile_screen/{userId}") { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                profile( navController,userId)
            }
            composable("post") { post(navController) }
            composable("chat/{userId},{otherUserId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType },
                    navArgument("otherUserId") { type = NavType.StringType })


            ) {
                val uid = it.arguments?.getString("userId").orEmpty()
                val oid = it.arguments?.getString("otherUserId").orEmpty()
                Log.d("Debug8", "Participants:${uid},${oid}")
               chatlist(uid,oid,navController)
            }
            composable("chat_screen/{channelId}",
                arguments = listOf(navArgument("channelId") { type = NavType.StringType }
                   )
            ) {
                val channelId = it.arguments?.getString("channelId").orEmpty()
                chatscreen(channelId,uid,navController)
            }
            //edit p
            composable("editProfile") {
                EditProfileScreen(uid,navController)
            }
// image full screen
          composable("fullScreenImage/{imageUrl}"){
              val imageUrl = it.arguments?.getString("imageUrl")
              if (imageUrl != null) {
                  FullScreenImageScreen(imageUrl,navController)
              }
          }

            /*
            composable("chat/{channelId}&{channelName}", arguments = listOf(

                navArgument("channelId") {
                    type = NavType.StringType
                },
                navArgument("channelName") {
                    type = NavType.StringType
                },

            )) {
                val channelId = it.arguments?.getString("channelId") ?: ""
                val channelName = it.arguments?.getString("channelName") ?: ""
                SpecificChatScreen(navController, channelId, viewModel )
            }

             */

            /*
            composable("chat_page2") {
                HomeScreen(navController) // Pass Room Database to ChatApp

            }

             */
            //chat scren ke liye
            /*
            composable(
                "chat_screen/{userId}/{userName}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType }, navArgument("userName") { type = NavType.StringType })
            ) {
                val userId = it.arguments?.getString("userId")!!
                val userName = it.arguments?.getString("userName")!!
                Chat(navController, userId, userName)
            }

             */
            /*
            composable(
                route = "item/{data}",
                arguments = listOf(
                    navArgument("data") { type = NavType.StringType }
                )
            ) {

                item = it.arguments?.getString("data") as rvmodel?
                SimplePropertyDetailsScreen(navController,item )

            }

             */

            composable(
                route = "details_screen?itemJson={itemJson}",
                arguments = listOf(
                    navArgument("itemJson") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val itemJson = backStackEntry.arguments?.getString("itemJson")
                val item = itemJson?.let { Gson().fromJson(it, rvmodel::class.java) }
                SimplePropertyDetailsScreen(navController, item)
            }


        }
    }

    //bootn ke clik pe ye fun run hoga or if ture hoga to andar jayenge
    //yaha jo chnages karnge vo bottom sheet par honga
    // Bottom sheet for showing options
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),

                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                //yaha se bottom sheet fun call ho rha hai jisme ye icon,name,navcontroler de rhe hai
                BottomSheetItem(
                    icon = Icons.Default.Add,
                    title = "Post"
                    ,navController

                ) {
                    showBottomSheet = false

                }

            }
        }
    }
}

//yaha vo fun hai ye banaya taki ek se jayada icon ho to is fun ko call karne par kaam ho jaye
@Composable
fun BottomSheetItem(
    icon: ImageVector,
    title: String,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.clickable {
            navController.navigate("post") {
                popUpTo("home_screen") { inclusive = false }
            }
        }
    ) {
        Icon(icon, contentDescription = null, tint = Color.Green)
        Text(text = title, color = Color.DarkGray, fontSize = 22.sp)
    }
}

