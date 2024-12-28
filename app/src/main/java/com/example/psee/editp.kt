package com.example.psee
// Jetpack Compose Core
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
                           //[rule navController.popBackStack() agar aise pop back likhe to button press par jaha se aaye vah vas chalejayenge]
// Firebase
import com.google.firebase.database.FirebaseDatabase

// Jetpack Compose UI Enhancements
import androidx.compose.ui.draw.clip

// For placeholder profile picture functionality
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.psee.bottombar.screens.FirebaseManager
import com.example.psee.bottombar.screens.User


@Composable
fun EditProfileScreen(
    userId: String,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var profileImageUrl by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch user data
    LaunchedEffect(userId) {
        FirebaseManager.fetchUser(userId) { user, error ->
            if (user != null) {
                name = user.name
                location = user.location
                bio = user.bio
                profileImageUrl = user.profileImageUrl
            } else {
                errorMessage = error
            }
            isLoading = false
        }
    }

    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Bar with Done and Cancel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Cancel", color = Color.Black)
                }
                TextButton(
                    onClick = {
                        val updatedUser = User(
                            id = userId,
                            name = name,
                            location = location,
                            bio = bio,
                            profileImageUrl = profileImageUrl
                        )

                        FirebaseManager.saveUser(updatedUser) { success, errorMessage ->
                            if (success) {
                                navController.popBackStack()
                            } else {
                                // Handle the error (e.g., show a toast or log the error)
                                errorMessage?.let {
                                    Log.e("SaveUser", it)
                                }
                            }
                        }
                    }
                ) {
                    Text(
                        text = "Done",
                        color = Color.Blue
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Image Section
            ProfileImage(
                url = profileImageUrl,
                isClickable = true,
                onImageSelected = { selectedUri ->
                    selectedUri?.let { uri ->
                        isLoading = true
                        FirebaseManager.uploadProfileImage(userId, uri) { imageUrl, error ->
                            isLoading = false
                            if (imageUrl != null) {
                                profileImageUrl = imageUrl
                            } else {
                                errorMessage = error ?: "Image upload failed"
                            }
                        }
                    }
                }
            )
            Text(
                text = "Change profile photo",
                color = Color.Blue,
                modifier = Modifier.clickable { /* Open photo picker */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Editable Fields
            EditableField(value = name, label = "Name") { name = it }
            Spacer(modifier = Modifier.height(8.dp))
            EditableField(value = location, label = "Location") { location = it }
            Spacer(modifier = Modifier.height(8.dp))
            EditableField(value = bio, label = "Bio") { bio = it }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun EditableField(value: String, label: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        maxLines = 1,
        singleLine = true
    )
}
@Composable
fun ProfileImage(
    url: String,
    isClickable: Boolean = false, // Default non-clickable
    onImageSelected: ((Uri?) -> Unit)? = null
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected?.invoke(uri)
    }

    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(Color.Gray)
            .then(
                if (isClickable) {
                    Modifier.clickable { launcher.launch("image/*") }
                } else {
                    Modifier // No click action
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (url.isNotEmpty()) {
            AsyncImage(
                model = url,
                contentDescription = "Profile Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default Profile",
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}


@Composable
fun FullScreenImageScreen(imageUrl: String, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clickable { navController.popBackStack() },
            contentScale = ContentScale.Fit
        )
    }
}


/*
@Composable
fun EditProfileScreen(
    userId: String,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var profileImageUrl by remember { mutableStateOf("") }

    // Fetch existing user data
    LaunchedEffect(Unit) {
        FirebaseManager.fetchUser(userId) { user ->
            if (user != null) {
                name = user.name
                location = user.location
                bio = user.bio
                profileImageUrl = user.profileImageUrl
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Profile Picture Placeholder
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Upload", modifier = Modifier.align(Alignment.Center))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Name Input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Location Input
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Bio Input
        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("About") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(
            onClick = {
                val updatedUser = User(
                    id = userId,
                    name = name,
                    location = location,
                    bio = bio,
                    profileImageUrl = profileImageUrl
                )
                FirebaseManager.saveUser(updatedUser) { success ->
                    if (success) {
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Save")
        }
    }
}


 */