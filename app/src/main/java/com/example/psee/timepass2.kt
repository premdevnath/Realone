package com.example.psee

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
// Foundation (Layouts, Gestures, etc.)


// Material Design Components

import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

// Runtime (State Management)

// UI Utilities

// Coil for Async Image Loading
import coil.compose.AsyncImage



import com.example.psee.dataclass.rvmodel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimplePropertyDetailsScreen(navController: NavController, item: rvmodel?) {
   // val viewModel = remember { ChatViewModel() }

    //
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Image Section
        AsyncImage(
            model = item?.imageUrl,
            contentDescription = "Property Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        // Back Button
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
            contentDescription = "Back",
            modifier = Modifier
                .padding(16.dp)
                .size(30.dp) // Adjusted size
                .clip(CircleShape)
                .background(Color.White) // Added white circular background
                .clickable {
                    navController.navigate("home_screen")
                }
                .align(Alignment.TopStart), // Fixed alignment issue
            tint = Color.Black
        )

        // Card Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter), // Place the card at the bottom
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Title and Message Image
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item?.Name ?: "Villa",
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )

                    // Message Icon
                    Log.d("hi1", "SimplePropertyDetailsScreen: ")
                    Image(
                        painter = painterResource(id = R.drawable.chat), // Replace with your image
                        contentDescription = "Message",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)

                            //chat scren khulega
                            //[agar ki var me value ho to aise nikalte hai foreach karke]

                            .clickable {


                             //   val otherUserId = item?.ownerId

                                /*

                                if (!userId.isNullOrEmpty() && !otherUserId.isNullOrEmpty()) {
                                    // Unique channel ID generate karo
                                    val channelId = if (userId < otherUserId) "$userId-$otherUserId" else "$otherUserId-$userId"
                                    navController.navigate("chat/$channelId,$userId")
                                } else {
                                    Log.e("NavigationError", "User IDs cannot be null.")
                                }


                                 */



//                                   sahi
                                /*

                                if (!userId.isNullOrEmpty() && !otherUserId.isNullOrEmpty()) {
                                    navController.navigate("chat/$userId,$otherUserId")
                                } else {
                                    // Handle the null case gracefully, maybe show a Toast or log an error
                                    Log.e("NavigationError", "User IDs cannot be null.")
                                }


                                 */



                                /*
                                val newChannelId = viewModel.getChannelId(userId, user.uid)
                                navController.navigate("chat/$newChannelId")


                                 */
                                /*old
                                channels.value.forEach { channel ->
                                    // Navigate to the chat screen of a specific channel

                                    navController.navigate("chat/")

                                }

                                 */

                            }


                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Price
                Text(
                    text = "₹${item?.price ?: "2,300,000"}",
                    style = MaterialTheme.typography.h5.copy(
                        color = Color(0xFF4A90E2),
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = item?.description ?: "3 BHK fully furnished villa.",
                    style = MaterialTheme.typography.body2.copy(
                        color = Color.Gray,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily.Serif // Improved font style
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Location
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = "Location Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item?.location ?: "Akashka Resort",
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace // Improved font style
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Continue Button
                Button(
                    onClick = {
                        val postOwnerId = item?.ownerId ?: ""
                        navController.navigate("profile_screen/$postOwnerId")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1E88E5))
                ) {
                    Text(
                        text = "Connect",
                        color = Color.White,
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
    }
}

/*
@Composable
fun SimplePropertyDetailsScreen(navController: NavController, item: rvmodel?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Back Button
        // Back Button
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
            contentDescription = "Back",
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp) // Increased size for better visibility
                .clip(CircleShape)
                .background(Color.White) // White circular background
                .zIndex(1f) // Bring to the front
                .align(Alignment.TopStart)
                .clickable {
                    navController.navigate("home_screen")
                },
            tint = Color.Black // Black color for better contrast
        )


        Column {
            // Image Section
            AsyncImage(
                model = item?.imageUrl,
                contentDescription = "Property Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            // Card Section
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-30).dp) // Overlapping effect
                    .padding(horizontal = 0.dp), // No side padding
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), // Rounded top
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp) // Inner padding
                ) {
                    // Title and Message Icon Row
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = item?.Name ?: "Villa",
                            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )

                        // Message Icon
                        Icon(
                            painter = painterResource(id = R.drawable.chat),
                            contentDescription = "Message",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    // Handle message click
                                },
                            tint = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Price
                    Text(
                        text = "₹${item?.price ?: "2,300,000"}",
                        style = MaterialTheme.typography.h5.copy(color = Color(0xFF4A90E2)),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    Text(
                        text = item?.description ?: "3 BHK fully furnished villa.",
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Location
                    Text(
                        text = item?.location ?: "Akashka Resort",
                        style = MaterialTheme.typography.body2.copy(color = Color.Gray)
                    )

                    Spacer(modifier = Modifier.weight(1f)) // Push the button to the bottom

                    // Continue Button
                    Button(
                        onClick = {
                            // Handle button click
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1E88E5))
                    ) {
                        Text(
                            text = "Continue",
                            color = Color.White,
                            style = MaterialTheme.typography.button
                        )
                    }
                }
            }
        }
    }
}


 */

@Composable
fun IconWithText(icon: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.body2.copy(color = Color.Gray)
        )
    }
}


