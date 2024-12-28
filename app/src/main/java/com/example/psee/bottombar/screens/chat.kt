package com.example.psee.bottombar.screens


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.psee.openChatScreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.psee.R

// Define the ChatItem data class
data class ChatItem(
    val name: String,
    val channelId: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(currentUserId: String, navController: NavController) {



    val db = FirebaseDatabase.getInstance().reference
    val chats = remember { mutableStateListOf<ChatItem>() }
    val isLoading = remember { mutableStateOf(true) }
    val placeholderImage = painterResource(id = R.drawable.mask)

    val searchQuery = remember { mutableStateOf("") }
    val filteredChats = chats.filter {
        it.name.contains(searchQuery.value, ignoreCase = true)
    }



    Log.d("Debug21", "Participants")
    LaunchedEffect(Unit) {
        db.child("Channels").get().addOnSuccessListener { snapshot ->
            chats.clear()
            snapshot.children.forEach { channel ->
                val participants = channel.child("participants").value as? List<*> ?: return@forEach
                val participantIds = participants.mapNotNull { it as? String } // Safely cast each item to String
                Log.d("Debug22", "Parsed Participants: $participantIds, CurrentUserId: $currentUserId")
                if (currentUserId in participants) {
                    val otherUserId = participants.find { it != currentUserId } as? String
                    Log.d("Debug9", "Participants:${otherUserId}")
                    otherUserId?.let {
                        db.child("users").child(it).child("name")
                            .get()
                            .addOnSuccessListener { nameSnapshot ->
                                val name = nameSnapshot.value?.toString() ?: "Unknown User"
                                chats.add(ChatItem(name = name, channelId = channel.key.orEmpty()))
                            }
                            .addOnFailureListener {
                                Log.e("FirebaseError", "Failed to fetch user name: ${it.message}")
                            }
                    }
                }
                else
                {
                    Log.d("Debug24", "Participants:")
                }
            }
            isLoading.value = false
        }.addOnFailureListener {
            Log.e("FirebaseError", "Failed to fetch channels: ${it.message}")
            isLoading.value = false
        }

        fun onCancelled(error: DatabaseError) {
            Log.d("Debug9", "Participants:${error}")
            Log.e("FirebaseError", error.message)
        }
    }








    Column(Modifier.fillMaxSize().padding(8.dp)) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it }, // Update search query dynamically
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F3F4), RoundedCornerShape(8.dp)),
            placeholder = {
                Text(
                    text = "Search here...",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF37474F),
                unfocusedBorderColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Loading Spinner
        if (isLoading.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Chat List
            LazyColumn {
                items(filteredChats) { chatItem ->
                    ChatRow(chatItem, navController)
                }
            }
        }
    }
}

 
@Composable
fun ChatRow(chatItem: ChatItem, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { openChatScreen(chatItem.channelId, navController) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            Image(
                painter = rememberImagePainter(data = R.drawable.ic_launcher_foreground), // Replace with dynamic URL using Coil
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // User Name and Last Message Preview
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chatItem.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }
        }
    }
}


 /*


@Composable
fun NotificationScreen(currentUserId: String, navController: NavController) {
    val db = FirebaseDatabase.getInstance().reference
    val chats = remember { mutableStateListOf<ChatItem>() } // State-backed list
    val isLoading = remember { mutableStateOf(true) } // Track loading state
    val placeholderImage = painterResource(id = R.drawable.mask)

    // Fetch channels from Firebase
    LaunchedEffect(Unit) {
        db.child("Channels").get().addOnSuccessListener { snapshot ->
            chats.clear() // Clear previous data
            snapshot.children.forEach { channel ->
                val participants = channel.child("participants").value as? List<*> ?: return@forEach
                val uniqueParticipants = participants.distinct()
                Log.d("Debug22", "Parsed Participants: $uniqueParticipants, CurrentUserId: $currentUserId")
                if (currentUserId in participants) {
                    val otherUserId = participants.firstOrNull { it != currentUserId } as? String
                    otherUserId?.let {
                        // Fetch other user's name
                        db.child("users").child(it).child("name")
                            .get()
                            .addOnSuccessListener { nameSnapshot ->
                                val name = nameSnapshot.value.toString() ?: "Unknown User"
                                chats.add(ChatItem(name = name, channelId = channel.key.toString()))
                                Log.e("FirebaseError", "Failed to fetch user name: ${name}")
                            }
                    }
                }
                else
                {
                    Log.d("Debug404", "Participants:${participants},${currentUserId}")
                }
            }
            isLoading.value = false // Loading complete
        }.addOnFailureListener {
            isLoading.value = false // Handle failure
        }
    }

    // Main UI
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (isLoading.value) {
            // Show loading spinner
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        /*
        else if (chats.isNotEmpty()) {
            // Show placeholder when no chats exist
            Image(
                painter = placeholderImage,
                contentDescription = "No Chats",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(150.dp)
            )
            Text(
                text = "No chats available.",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray
            )
        }
        */
        else {
            // Show chat list
            LazyColumn {
                items(chats) { chatItem ->
                    ChatRow(chatItem, navController)
                }
            }
        }
    }
}



@Composable
fun ChatRow(chatItem: ChatItem, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { openChatScreen(chatItem.channelId, navController) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            Image(
                painter = rememberImagePainter(data = R.drawable.ic_launcher_foreground), // Replace with dynamic URL using Coil
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // User Name and Last Message Preview
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chatItem.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }
        }
    }
}


 */

///
/*
@Composable
fun NotificationScreen(currentUserId: String, navController: NavController) {
    val db = FirebaseDatabase.getInstance().reference
    val chats = remember { mutableStateListOf<ChatItem>() } // State-backed list
    val isLoading = remember { mutableStateOf(true) } // Track loading state
    val placeholderImage = painterResource(id = R.drawable.mask)

    // Fetch channels from Firebase
    LaunchedEffect(Unit) {
        db.child("Channels").get().addOnSuccessListener { snapshot ->
            chats.clear() // Clear previous data
            snapshot.children.forEach { channel ->
                val participants = channel.child("participants").value as? List<*> ?: return@forEach
                if (currentUserId in participants) {
                    val otherUserId = participants.firstOrNull { it != currentUserId } as? String
                    otherUserId?.let {
                        // Fetch other user's name
                        db.child("users").child(it).child("name")
                            .get()
                            .addOnSuccessListener { nameSnapshot ->
                                val name = nameSnapshot.value.toString()
                                chats.add(ChatItem(name = name, channelId = channel.key.toString()))
                            }
                    }
                }
            }
            isLoading.value = false // Loading complete
        }.addOnFailureListener {
            isLoading.value = false // Handle failure
        }
    }

    // Main UI
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (isLoading.value) {
            // Show loading spinner
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (chats.isEmpty()) {
            // Show placeholder when no chats exist
            Image(
                painter = placeholderImage,
                contentDescription = "No Chats",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(150.dp)
            )
            Text(
                text = "No chats available.",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray
            )
        } else {
            // Show chat list
            LazyColumn {
                items(chats) { chatItem ->
                    ChatRow(chatItem, navController)
                }
            }
        }
    }
}


 */

/*sahi

//real
@Composable
fun ChatRow(chatItem: ChatItem, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { openChatScreen(chatItem.channelId, navController) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Card background color
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp // Elevation for depth
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User image (placeholder or actual image)
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.LightGray, CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            // User name
            Log.e("FirebaseError", "Failed to fetch user name: ${chatItem.name}")
            Text(
                text = chatItem.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


 s*/

/*
@Composable
fun ChatRow(chatItem: ChatItem, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { openChatScreen(chatItem.channelId, navController) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = chatItem.name.ifBlank { "Loading..." }, // Temporary placeholder
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


 */
/*
@Composable
fun NotificationScreen(currentUserId: String, navController: NavController) {
    val db = FirebaseDatabase.getInstance().reference
    val chats = remember { mutableStateListOf<ChatItem>() } // State-backed list
    val placeholderImage = painterResource(id = R.drawable.mask)

    // Fetch channels from Firebase
    LaunchedEffect(Unit) {
        db.child("Channels").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chats.clear() // Clear previous data
                snapshot.children.forEach { channel ->
                    val participants = channel.child("participants").value as? List<*> ?: return
                    if (currentUserId in participants) {
                        val otherUserId = participants.firstOrNull { it != currentUserId } as? String
                        otherUserId?.let {
                            db.child("users").child(it).child("name")
                                .get()
                                .addOnSuccessListener { nameSnapshot ->
                                    val name = nameSnapshot.value.toString()
                                    chats.add(ChatItem(name = name, channelId = channel.key.toString()))
                                }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if necessary
            }
        })
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (chats.isEmpty()) {
            // Display placeholder when no chats exist
            Image(
                painter = placeholderImage,
                contentDescription = "No Chats",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(150.dp)
            )
            Text(
                text = "No chats available.",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray
            )
        } else {
            LazyColumn {
                items(chats) { chatItem -> // Access `ChatItem` properties
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { openChatScreen(chatItem.channelId, navController) }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = chatItem.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}


 */

/*//p
@Composable
fun chatlist(uid: String,oid: String,navController: NavController) {
    checkOrCreateChannel(uid, oid,navController)
}
fun checkOrCreateChannel(user1: String, user2: String, navController: NavController) {
    val db = FirebaseDatabase.getInstance().reference
    val channelId = if (user1 < user2) "$user1$user2" else "$user2$user1" // Unique ID
    db.child("Channels").child(channelId).get().addOnSuccessListener { snapshot ->
        if (!snapshot.exists()) {
            // Agar channel exist nahi karta, to naya channel create karo
            createNewChannel(db, channelId, user1, user2,navController)
        } else {
            // Agar channel exist karta hai, to chat screen open karo
            openChatScreen(channelId,navController)
        }
    }
}



fun createNewChannel(db: DatabaseReference, channelId: String, user1: String, user2: String,navController: NavController) {
    val channelData = mapOf(
        "lastMessage" to "",
        "timestamp" to System.currentTimeMillis().toString(),
        "participants" to listOf(user1, user2)
    )
    db.child("Channels").child(channelId).setValue(channelData).addOnSuccessListener {
        // Channel successfully created, ab chat screen open karo
        openChatScreen(channelId,navController)
    }
}
fun openChatScreen(channelId: String,navController: NavController)
{
    navController.navigate("chat_screen/$channelId")
}


 */
///
/*old is
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current.applicationContext
    val viewModel = remember { HomeViewModel() }
    val channels = viewModel.channels.collectAsState()
    val addChannel = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { addChannel.value = true }) {
                Text(text = "Add Channel", color = Color.White)
            }
        },
        containerColor = Color.Black
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn {
                item {
                    Text(
                        text = "Messages",
                        color = Color.Gray,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    SearchBar()
                }
                items(channels.value) { channel ->
                    ChannelItem(
                        channelName = channel.name,
                        modifier = Modifier.padding(16.dp),
                        onClick = { navController.navigate("chat/${channel.id}&${channel.name}") }
                    )
                }
            }
        }
    }

    if (addChannel.value) {
        ModalBottomSheet(
            onDismissRequest = { addChannel.value = false },
            sheetState = sheetState
        ) {
            AddChannelDialog {
                viewModel.addChannel(it)
                addChannel.value = false
            }
        }
    }
}

@Composable
fun SearchBar() {
    TextField(
        value = "",
        onValueChange = {},
        placeholder = { Text(text = "Search...") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(40.dp)),
        textStyle = TextStyle(color = Color.LightGray),
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = DarkGrey,
            unfocusedContainerColor = DarkGrey
        ),
        trailingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) }
    )
}

@Composable
fun ChannelItem(channelName: String, modifier: Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkGrey)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Yellow.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = channelName.first().uppercase(),
                color = Color.White,
                style = TextStyle(fontSize = 24.sp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = channelName,
            color = Color.White,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun AddChannelDialog(onAddChannel: (String) -> Unit) {
    val channelName = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Channel",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = channelName.value,
            onValueChange = { channelName.value = it },
            label = { Text(text = "Channel Name") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onAddChannel(channelName.value) }) {
            Text(text = "Add")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItem() {
    ChannelItem(channelName = "Test Channel", modifier = Modifier) {}
}



 */





















































////////
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current as MainActivity
    val viewModel = hiltViewModel<HomeViewModel>()
    val channels = viewModel.channels.collectAsState()
    val addChannel = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    /*
    //video call ke liye
    LaunchedEffect(Unit) {
        Firebase.auth.currentUser?.let {
            context.initZegoService(
                appID = AppID,
                appSign = AppSign,
                userID = it.email!!,
                userName = it.email!!
            )
        }
    }



     */

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { addChannel.value = true }) {
                Text(text = "Add Channel", color = Color.White)
            }
        },
        containerColor = Color.Black
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            LazyColumn {
                item {
                    Text(
                        text = "Messages",
                        color = Color.Gray,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    SearchBar()
                }
                //kisi card pe clik karete hi data leke ja rhaa hai
                items(channels.value) { channel ->
                    ChannelItem(
                        channelName = channel.name,
                        modifier = Modifier.padding(16.dp),
                        onClick = { navController.navigate("chat/${channel.id}&${channel.name}") }
                    )
                }
            }
        }
    }

    if (addChannel.value) {
        ModalBottomSheet(
            onDismissRequest = { addChannel.value = false },
            sheetState = sheetState
        ) {
            AddChannelDialog {
                viewModel.addChannel(it)
                addChannel.value = false
            }
        }
    }
}

@Composable
fun SearchBar() {
    TextField(
        value = "",
        onValueChange = {},
        placeholder = { Text(text = "Search...") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(40.dp)),
        textStyle = TextStyle(color = Color.LightGray),
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = DarkGrey,
            unfocusedContainerColor = DarkGrey,
            focusedTextColor = DarkGrey,
            unfocusedTextColor = DarkGrey,
            focusedPlaceholderColor = DarkGrey,
            unfocusedPlaceholderColor = DarkGrey,
            focusedIndicatorColor = DarkGrey
        ),
        trailingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) }
    )
}

@Composable
fun ChannelItem(channelName: String, modifier: Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkGrey)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Yellow.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = channelName.first().uppercase(),
                color = Color.White,
                style = TextStyle(fontSize = 24.sp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = channelName,
            color = Color.White,
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun AddChannelDialog(onAddChannel: (String) -> Unit) {
    val channelName = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Channel", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = channelName.value,
            onValueChange = { channelName.value = it },
            label = { Text(text = "Channel Name") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onAddChannel(channelName.value) }) {
            Text(text = "Add")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItem() {
    ChannelItem(channelName = "Test Channel", modifier = Modifier) {}
}





 */