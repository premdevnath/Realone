package com.example.psee.bottombar.screens

import androidx.compose.runtime.Composable
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.psee.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun post(navController: NavController) {

    val context = LocalContext.current

    // State variables
    var Name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Firebase references
    val auth = FirebaseAuth.getInstance()
    val databaseRef = FirebaseDatabase.getInstance().reference
    val storageRef = FirebaseStorage.getInstance().reference
    // Launch image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Item", color = Color.White) },
                backgroundColor = Color(0xFF262626),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Image Picker at Top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFF262626))
                    .border(1.dp, Color.Gray, RoundedCornerShape(5.dp))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberImagePainter(data = selectedImageUri),
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Tap to select image",
                        tint = Color.Gray,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            // Name Field
            OutlinedTextField(
                value = Name,
                onValueChange = { Name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            // Price Field
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            // Location Field
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            // Save Button at Bottom
            Button(
                onClick = {
                    if (Name.isNotBlank() && price.isNotBlank() && location.isNotBlank() && description.isNotBlank() && selectedImageUri != null) {
                        uploadDataToFirebase(
                            context = context,
                            Name = Name,
                            price = price,
                            location = location,
                            description = description,
                            imageUri = selectedImageUri!!,
                            databaseRef = databaseRef,
                            storageRef = storageRef
                        )
                        navController.navigate("home")
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF32DC3A))
            ) {
                Text(text = "POST", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Item", color = Color.White) },
                backgroundColor = Color(0xFF262626),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Name Field
            OutlinedTextField(
                value = Name,
                onValueChange = { Name = it },
                label = { Text("Name", color = Color.Gray) },
                textStyle = TextStyle(color = Color.Gray),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )

            // Price Field
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price", color = Color.Gray) },
                textStyle = TextStyle(color = Color.Gray),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )

            // Location Field
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location", color = Color.Gray) },
                textStyle = TextStyle(color = Color.Gray),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )

            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description", color = Color.Gray) },
                textStyle = TextStyle(color = Color.Gray),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )

            // Image Selection Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFFFFFFFF))
                    .border(1.dp, Color.Gray, RoundedCornerShape(5.dp))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberImagePainter(data = selectedImageUri),
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.uplod),
                        contentDescription = "Tap to select image",
                        modifier = Modifier.size(50.dp)
                    )
                    /*
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Tap to select image",
                        tint = Color.Gray,
                        modifier = Modifier.size(50.dp)
                    )

                     */
                }
            }
            Button(
                onClick = {
                    if (Name.isNotBlank() && price.isNotBlank() && location.isNotBlank() && description.isNotBlank() && selectedImageUri != null) {
                        uploadDataToFirebase(
                            context = context,
                            Name = Name,
                            price = price,
                            location = location,
                            description = description,
                            imageUri = selectedImageUri!!,
                            databaseRef = databaseRef,
                            storageRef = storageRef
                        )
                        navController.navigate("home_screen")
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT)
                            .show()
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF32DC3A))
            ) {
                Text(text = "Post", fontSize = 18.sp, color = Color.White)
            }

            /*
            // Save Button
            Button(
                onClick = {
                    if (Name.isNotBlank() && price.isNotBlank() && location.isNotBlank() && description.isNotBlank() && selectedImageUri != null) {
                        uploadDataToFirebase(
                            context = context,
                            Name = Name,
                            price = price,
                            location = location,
                            description = description,
                            imageUri = selectedImageUri!!,
                            databaseRef = databaseRef,
                            storageRef = storageRef
                        )
                        navController.navigate("home")
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFE6B042))
            ) {
                Text(text = "Post", color = Color.Black, fontWeight = FontWeight.Bold)
            }

             */
        }
    }
}




fun uploadDataToFirebase(
    context: Context,
    Name: String,
    price: String,
    location: String,
    description: String,
    imageUri: Uri,
    databaseRef: DatabaseReference,
    storageRef: StorageReference
) {
    //changes p
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid // Current user ka UID
    if (currentUserId == null) {
        Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show()
        return
    }

    val itemKey = databaseRef.child("items").push().key
    val imageRef = storageRef.child("item_images/$itemKey.jpg")

    imageRef.putFile(imageUri).addOnSuccessListener {
        imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
            val itemData = mapOf(
                "Name" to Name,
                "price" to price,
                "location" to location,
                "description" to description,
                "imageUrl" to downloadUrl.toString(), //chnagesp
                "ownerId" to currentUserId // Owner ka UID yaha add kiya
            )
            itemKey?.let { key ->
                databaseRef.child("items").child(key).setValue(itemData)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Item added successfully!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to add item.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }.addOnFailureListener {
        Toast.makeText(context, "Image upload failed.", Toast.LENGTH_SHORT).show()
    }
}


/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun post(navController: NavController) {


        val context = LocalContext.current

        // State variables
        var Name by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }
        var location by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

        // Firebase references
        val auth = FirebaseAuth.getInstance()
        val databaseRef = FirebaseDatabase.getInstance().reference
        val storageRef = FirebaseStorage.getInstance().reference

        // Launch image picker
        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            selectedImageUri = uri
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Add New Item") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                /*
                // Food Name Input
                TextField(
                    value = Name,
                    onValueChange = { Name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                 */
                // Name Field
                OutlinedTextField(
                    value = Name, onValueChange = { Name = it },
                    label = { Text("Name", color = Color.Gray) },
                    textStyle = TextStyle(color = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(65.dp)
                )

                OutlinedTextField(
                    value = price, onValueChange = { price = it },
                    label = { Text("Price", color = Color.Gray) },
                    textStyle = TextStyle(color = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(65.dp)
                )

                // Location Input

                OutlinedTextField(
                    value = location, onValueChange = {location = it },
                    label = { Text("Location", color = Color.Gray) },
                    textStyle = TextStyle(color = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(65.dp)
                )

                // Description Input
                OutlinedTextField(
                    value = description, onValueChange = { description = it },
                    label = { Text("Description", color = Color.Gray) },
                    textStyle = TextStyle(color = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(65.dp)
                )

                // Image Preview and Upload Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, Color.Gray)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberImagePainter(data = selectedImageUri),
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text("Tap to select an image")
                    }
                }
                Button(
                    onClick = {
                        if (Name.isNotBlank() && price.isNotBlank() && location.isNotBlank() && description.isNotBlank() && selectedImageUri != null) {
                            uploadDataToFirebase(
                                context = context,
                                Name = Name,
                                price = price,
                                location = location,
                                description = description,
                                imageUri = selectedImageUri!!,
                                databaseRef = databaseRef,
                                storageRef = storageRef
                            )
                            navController.navigate("home")
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(55.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF32DC3A))
                ) {
                    Text(text = "Add items", fontSize = 18.sp, color = Color.White)
                }

            }
        }
    }
*/





fun uploadDataToFirebase(
    context: Context,
    Name: String,
    price: String,
    location: String,
    description: String,
    imageUri: Uri,
    databaseRef: DatabaseReference,
    storageRef: StorageReference
) {
    //changes p
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid // Current user ka UID
    if (currentUserId == null) {
        Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show()
        return
    }

    val itemKey = databaseRef.child("items").push().key
    val imageRef = storageRef.child("item_images/$itemKey.jpg")

    imageRef.putFile(imageUri).addOnSuccessListener {
        imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
            val itemData = mapOf(
                "Name" to Name,
                "price" to price,
                "location" to location,
                "description" to description,
                "imageUrl" to downloadUrl.toString(), //chnagesp
                "ownerId" to currentUserId // Owner ka UID yaha add kiya
            )
            itemKey?.let { key ->
                databaseRef.child("items").child(key).setValue(itemData)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Item added successfully!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to add item.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }.addOnFailureListener {
        Toast.makeText(context, "Image upload failed.", Toast.LENGTH_SHORT).show()
    }
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(navController: NavController) {
    val context = LocalContext.current

    // State variables
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launch image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Post") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Image Selection Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberImagePainter(data = selectedImageUri),
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Tap to select an image", color = Color.Gray)
                }
            }

            // Price Input
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Location Input
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )

            // Description Input
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            // Submit Button
            Button(
                onClick = {
                    if (price.isNotBlank() && location.isNotBlank() && description.isNotBlank() && selectedImageUri != null) {
                        Toast.makeText(context, "Post shared successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Share", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

 */