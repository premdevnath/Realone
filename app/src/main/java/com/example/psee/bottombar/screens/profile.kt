package com.example.psee.bottombar.screens
// Jetpack Compose Core
import android.R.attr.text
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Firebase
import com.google.firebase.database.FirebaseDatabase

// Coil for Image Loading
import coil.compose.rememberImagePainter

// Other Jetpack Compose UI Components
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import com.example.psee.ProfileImage
import com.example.psee.dataclass.rvmodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


data class User(
    val id: String = "",
    val name: String = "",
    val profileImageUrl: String = "",
    val location: String = "",
    val bio: String = ""
)
@Composable
fun profile(
    navController: NavController,
    userId: String
) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val profileId = remember { userId }

    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val listData = remember { mutableStateListOf<rvmodel>() }
    // Fetch the profile data
    LaunchedEffect(profileId) {
        if (profileId == currentUserId) {
            // Fetch logged-in user's profile
            FirebaseManager.fetchCurrentUser { fetchedUser, error ->
                user = fetchedUser
                errorMessage = error
                isLoading = false
            }
        } else {
            // Fetch another user's profile
            FirebaseManager.fetchUser(profileId) { fetchedUser, error ->
                user = fetchedUser
                errorMessage = error
                isLoading = false
            }
        }
    }
//data
    LaunchedEffect(Unit) {
// Retrieve data from Firebase
        dataRetrieve2(listData)

// Ensure listData is updated normally without reversing
        listData.clear() // Clear the original list to avoid duplicates
        listData.addAll(listData) // Add items directly in normal order

    }
    if (isLoading) {
        // Show a loading indicator
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Display the profile or error message
        user?.let { userData ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Image
                ProfileImage(url = userData.profileImageUrl)

                Spacer(modifier = Modifier.height(8.dp))

                // Name
                Text(
                    text = userData.name,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
                )

                // Bio
                Text(
                    text = userData.bio,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // If the user is the current user, show the Edit Profile button
                if (profileId == currentUserId) {
                    Button(
                        onClick = { navController.navigate("editProfile") },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(0.6f)
                    ) {
                        Text("Edit Profile")
                    }
                }
                else
                {
                    if(profileId != currentUserId)
                    {
                        Button(
                            onClick = {
                                val otherUserId = listData.filterNotNull()
                                    .distinctBy { it.ownerId }
                                    .firstOrNull { it.ownerId != userId }?.ownerId // Pick the first valid user
                                if (!userId.isNullOrEmpty() && !otherUserId.isNullOrEmpty()) {
                                    Log.d("Debug7", "Navigating with Participants: $userId, $otherUserId")
                                    navController.navigate("chat/$userId,$otherUserId")
                                } else {
                                    Log.e("NavigationError", "No valid participants found.")
                                }
                            },
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(0.6f)
                        ) {
                            Text("Message")
                        }

                        /*
                        Button(
                            onClick = {
                                listData.forEach { item ->
                                    val otherUserId = item?.ownerId ?: ""
                                    if (!userId.isNullOrEmpty() && !otherUserId.isNullOrEmpty()) {
                                        navController.navigate("chat/$userId,$otherUserId")
                                        Log.d("Debug7", "Participants:${userId},${otherUserId}")
                                    } else {
                                        // Handle the null case gracefully, maybe show a Toast or log an error
                                        Log.e("NavigationError", "User IDs cannot be null.")
                                    }
                                }


                                      },
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(0.6f)
                        ) {
                            Text("massage")
                        }

                         */
                    }
                }
            }
        } ?: run {
            // If user data is null, display the error message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: "Unable to load profile",
                    color = Color.Red,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}

@Composable
fun ProfileImage(url: String) {
    Image(
        painter = rememberAsyncImagePainter(model = url),
        contentDescription = "Profile Image",
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .border(2.dp, Color.Gray, CircleShape)
    )
}
//data retirve

fun dataRetrieve2(listData: MutableList<rvmodel>) {
    val database = FirebaseDatabase.getInstance()
    val dataRef = database.reference.child("items")

    dataRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val uniqueList = mutableSetOf<rvmodel>() // Use a set to ensure uniqueness
            for (foodSnapshot in snapshot.children) {
                foodSnapshot.getValue(rvmodel::class.java)?.let { uniqueList.add(it) }
            }
            listData.clear()
            listData.addAll(uniqueList) // Add unique entries to listData
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("DataRetrieveError", error.message)
        }
    })
}

/*
// Function to retrieve data from Firebase and update the list
fun dataRetrieve2(listData: MutableList<rvmodel>) {

    val database = FirebaseDatabase.getInstance()
    val dataRef = database.reference.child("items")

    dataRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {

            listData.clear() // Clear existing data
            for (foodSnapshot in snapshot.children) {

                foodSnapshot.getValue(rvmodel::class.java)?.let {
                    listData.add(it) // Add the fetched item to the list

                    //    listData.reversed()
                }
            }

        }

        override fun onCancelled(error: DatabaseError) {

        }
    })
}

 */


/*sahi
@Composable
fun profile(
    userId: String,
    navController: NavController
) {
    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        FirebaseManager.fetchUser(userId) { fetchedUser, error ->
            user = fetchedUser
            errorMessage = error
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display error if present
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(8.dp)
                )
            }

            user?.let { currentUser ->
                // Profile Image
                user?.let { currentUser ->
                    ProfileImage(
                        url = currentUser.profileImageUrl,
                        isClickable = false
                    )


                    Spacer(modifier = Modifier.height(8.dp))

                    // Name
                    Text(
                        text = currentUser.name,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    // Location
                    Text(
                        text = currentUser.location,
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Bio
                    Text(
                        text = currentUser.bio,
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row() {

                        Button(
                            onClick = { navController.navigate("editProfile") },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .clip(RoundedCornerShape(8.dp)),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3897F0)) // Instagram blue
                        ) {
                            Text(
                                text = "Edit Profile",
                                color = Color.White,
                                style = MaterialTheme.typography.button
                            )
                        }

                    }
                }
            }
        }
    }
}

 */
//
/*
@Composable
fun profile(
    userId: String,
    navController: NavController
) {
    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch user data from Firebase
    LaunchedEffect(Unit) {
        FirebaseManager.fetchUser(userId) { fetchedUser ->
            user = fetchedUser
            isLoading = false // Stop loading once data is fetched
        }
    }

    if (isLoading) {
        // Show a loading spinner while data is being fetched
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        user?.let { currentUser ->
            // Display profile UI if the user data is fetched
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Profile Picture
                Image(
                    painter = rememberImagePainter(data = currentUser.profileImageUrl),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Name
                Text(
                    text = currentUser.name,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                // Location
                Text(
                    text = currentUser.location,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // About Section
                Text(
                    text = currentUser.bio,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Edit Profile Button
                Button(
                    onClick = { navController.navigate("editProfile") },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Edit Profile")
                }
            }
        } ?: run {
            // Show an error message if the user is not found
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("User not found. Please try again.")
            }
        }
    }
}


 */