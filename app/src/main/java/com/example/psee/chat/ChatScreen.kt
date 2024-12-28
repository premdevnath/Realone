package com.example.psee.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.psee.model.Message
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.messaging.FirebaseMessaging






//with fcm
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun chatscreen(channelId: String, currentUserId: String, navController: NavController, viewModel: ChatModel = remember { ChatModel() }) {
    val messages by viewModel.messages.collectAsState()
    val messageText = remember { mutableStateOf("") }
    val listState = rememberLazyListState()


    val context = LocalContext.current

    // Listen to messages when the screen is opened
    // Listen to new messages

    LaunchedEffect(channelId) {
        Log.d("Debug36", "Participants: ${channelId}")
        viewModel.listenToMessages(channelId)
        Log.d("Debug37", "Participants: ${channelId}")
        viewModel.subscribeForNotification(channelId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sachin") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Open menu */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                // Messages list
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    reverseLayout = true,
                    state = listState
                ) {
                    items(messages) { message ->
                        MessageBubble(message, currentUserId)
                    }
                }

                // Message input and send button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText.value,
                        onValueChange = { messageText.value = it },
                        placeholder = { Text("Type a message...") },
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(24.dp))
                            .padding(horizontal = 8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (messageText.value.trim().isNotEmpty()) {
                                viewModel.sendMessage(channelId, currentUserId, messageText.value.trim(),context)
                                messageText.value = ""
                            }
                        }
                    ) {
                        Text("Send")
                    }
                }
            }
        }
    )
}

// Message Bubble ka design
@Composable
fun MessageBubble(message: Message, currentUserId: String) {
    val isCurrentUser = message.senderId == currentUserId
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (isCurrentUser) Color(0xFFDCF8C6) else Color(0xFFFFFFFF)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(text = message.text, color = Color.Black)
        }
    }
}



/*without fcm
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun chatscreen(channelId: String, currentUserId: String, navController: NavController) {
    val db = FirebaseDatabase.getInstance().reference
    val messages = remember { mutableStateListOf<Message>() }
    val messageText = remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Listen to new messages
    LaunchedEffect(channelId) {
        db.child("Messages").child(channelId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    messages.add(0, message) // Latest message at the bottom
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Scroll to the bottom when messages change
    LaunchedEffect(messages.size) {
        listState.scrollToItem(0)
    }

    // UI Layout
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sachin") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Open menu */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                // Messages list
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    reverseLayout = true,
                    state = listState
                ) {
                    items(messages) { message ->
                        MessageBubble(message, currentUserId)
                    }
                }

                // Message input and send button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText.value,
                        onValueChange = { messageText.value = it },
                        placeholder = { Text("Type a message...") },
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(24.dp)) // Background with rounded corners
                            .padding(horizontal = 8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent, // Set this to avoid default background
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (messageText.value.trim().isNotEmpty()) {
                                sendMessage(db, channelId, currentUserId, messageText.value.trim())
                                messageText.value = ""
                            }
                        }
                    ) {
                        Text("Send")
                    }
                }
            }
        }
    )
}
// Message Bubble ka design
@Composable
fun MessageBubble(message: Message, currentUserId: String) {
    val isCurrentUser = message.senderId == currentUserId
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (isCurrentUser) Color(0xFFDCF8C6) else Color(0xFFFFFFFF)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(text = message.text, color = Color.Black)
        }
    }
}

// Send message function
fun sendMessage(db: DatabaseReference, channelId: String, senderId: String, text: String) {
    if (text.isNotBlank()) {
        val messageId = db.child("Messages").child(channelId).push().key ?: return
        val message = Message(senderId, text, System.currentTimeMillis())
        db.child("Messages").child(channelId).child(messageId).setValue(message)
    }
}

 */

/*

 */




/*
//without fcm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun chatscreen(channelId: String, currentUserId: String, navController: NavController) {
    val db = FirebaseDatabase.getInstance().reference
    val messages = remember { mutableStateListOf<Message>() }
    val messageText = remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Listen to new messages
    LaunchedEffect(channelId) {
        db.child("Messages").child(channelId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    messages.add(0, message) // Latest message at the bottom
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Scroll to the bottom when messages change
    LaunchedEffect(messages.size) {
        listState.scrollToItem(0)
    }

    // UI Layout
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sachin") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Open menu */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                // Messages list
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    reverseLayout = true,
                    state = listState
                ) {
                    items(messages) { message ->
                        MessageBubble(message, currentUserId)
                    }
                }

                // Message input and send button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText.value,
                        onValueChange = { messageText.value = it },
                        placeholder = { Text("Type a message...") },
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(24.dp)) // Background with rounded corners
                            .padding(horizontal = 8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent, // Set this to avoid default background
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (messageText.value.trim().isNotEmpty()) {
                                sendMessage(db, channelId, currentUserId, messageText.value.trim())
                                messageText.value = ""
                            }
                        }
                    ) {
                        Text("Send")
                    }
                }
            }
        }
    )
}
// Message Bubble ka design
@Composable
fun MessageBubble(message: Message, currentUserId: String) {
    val isCurrentUser = message.senderId == currentUserId
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (isCurrentUser) Color(0xFFDCF8C6) else Color(0xFFFFFFFF)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(text = message.text, color = Color.Black)
        }
    }
}


// Send message function
fun sendMessage(db: DatabaseReference, channelId: String, senderId: String, text: String) {
    if (text.isNotBlank()) {
        val messageId = db.child("Messages").child(channelId).push().key ?: return
        val message = Message(senderId, text, System.currentTimeMillis())
        db.child("Messages").child(channelId).child(messageId).setValue(message)
    }
}




 */





/////////////////////////////////////////////////////////////////

//new ui

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun chatscreen(channelId: String, currentUserId: String, navController: NavController) {
    val db = FirebaseDatabase.getInstance().reference
    val messages = remember { mutableStateListOf<Message>() }
    val messageText = remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Listen to new messages
    LaunchedEffect(channelId) {
        db.child("Messages").child(channelId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    messages.add(0, message) // Latest message at the bottom
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Scroll to the bottom when messages change
    LaunchedEffect(messages.size) {
        listState.scrollToItem(0)
    }

    // UI Layout
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sachin") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Open menu */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                // Messages list
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    reverseLayout = true,
                    state = listState
                ) {
                    items(messages) { message ->
                        MessageBubble(message, currentUserId)
                    }
                }

                // Message input and send button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText.value,
                        onValueChange = { messageText.value = it },
                        placeholder = { Text("Type a message...") },
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(24.dp)) // Background with rounded corners
                            .padding(horizontal = 8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent, // Set this to avoid default background
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (messageText.value.trim().isNotEmpty()) {
                                sendMessage(db, channelId, currentUserId, messageText.value.trim())
                                messageText.value = ""
                            }
                        }
                    ) {
                        Text("Send")
                    }
                }
            }
        }
    )
}
// Message Bubble ka design
@Composable
fun MessageBubble(message: Message, currentUserId: String) {
    val isCurrentUser = message.senderId == currentUserId
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (isCurrentUser) Color(0xFFDCF8C6) else Color(0xFFFFFFFF)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(text = message.text, color = Color.Black)
        }
    }
}

// Send message function
fun sendMessage(db: DatabaseReference, channelId: String, senderId: String, text: String) {
    if (text.isNotBlank()) {
        val messageId = db.child("Messages").child(channelId).push().key ?: return
        val message = Message(senderId, text, System.currentTimeMillis())
        db.child("Messages").child(channelId).child(messageId).setValue(message)
    }
}


 */







/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun chatscreen(channelId: String, currentUserId: String, navController: NavController) {
    val db = FirebaseDatabase.getInstance().reference
    val messages = remember { mutableStateListOf<Message>() }
    val messageText = remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Naye messages suno aur add karo
    LaunchedEffect(channelId) {
        db.child("Messages").child(channelId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    messages.add(0, message) // Latest message neeche dikhayenge
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Jab message count change ho, neeche scroll ho
    LaunchedEffect(messages.size) {
        listState.scrollToItem(0)
    }

    // UI Layout
    Column(modifier = Modifier.fillMaxSize()) {
        // Messages ka list
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true, // Naye messages neeche dikhayenge
            state = listState
        ) {
            items(messages) { message ->
                MessageBubble(message, currentUserId)
            }
        }

        // Message input aur send button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageText.value,
                onValueChange = { messageText.value = it },
                placeholder = { Text("Type a message...") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (messageText.value.trim().isNotEmpty()) { // Blank messages na bhejein
                        sendMessage(db, channelId, currentUserId, messageText.value.trim())
                        messageText.value = ""
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}

// Message Bubble ka design
@Composable
fun MessageBubble(message: Message, currentUserId: String) {
    val isCurrentUser = message.senderId == currentUserId
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (isCurrentUser) Color(0xFFDCF8C6) else Color(0xFFFFFFFF)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(text = message.text, color = Color.Black)
        }
    }
}

// Send message function
fun sendMessage(db: DatabaseReference, channelId: String, senderId: String, text: String) {
    if (text.isNotBlank()) {
        val messageId = db.child("Messages").child(channelId).push().key ?: return
        val message = Message(senderId, text, System.currentTimeMillis())
        db.child("Messages").child(channelId).child(messageId).setValue(message)
    }
}


 */



/*
fun chatscreen(channelId: String, currentUserId: String, navController: NavController) {
    val db = FirebaseDatabase.getInstance().reference
    val messages = remember { mutableStateListOf<Message>() }
    val messageText = remember { mutableStateOf("") }

    // Listen for new messages
    LaunchedEffect(channelId) {
        db.child("Messages").child(channelId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    messages.add(message)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                MessageBubble(message, currentUserId)
            }
        }

        // Input Field for Sending Messages
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageText.value,
                onValueChange = { messageText.value = it },
                placeholder = { Text("Type a message...") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                sendMessage(db, channelId, currentUserId, messageText.value)
                messageText.value = ""
            }) {
                Text("Send")
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, currentUserId: String) {
    val isCurrentUser = message.senderId == currentUserId
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (isCurrentUser) Color(0xFFDCF8C6) else Color(0xFFFFFFFF)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        contentAlignment = alignment
    ) {
        Text(
            text = message.text,
            modifier = Modifier
                .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            color = Color.Black
        )
    }
}

fun sendMessage(db: DatabaseReference, channelId: String, senderId: String, text: String) {
    if (text.isNotBlank()) {
        val messageId = db.child("Messages").child(channelId).push().key ?: return
        val message = Message(senderId, text, System.currentTimeMillis())
        db.child("Messages").child(channelId).child(messageId).setValue(message)
    }
}



 */
/*
fun ChatScreen(
    receiverUserId: String,
    receiverUserName: String,
    viewModel: ChatViewModel
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val messages by viewModel.getMessages(receiverUserId).collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        // Display the partner's name at the top
        Text(
            text = "Chat with $receiverUserName",
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )

        // Messages List
        LazyColumn(
            modifier = Modifier.weight(1f).padding(8.dp),
            reverseLayout = true // Show recent messages at the bottom
        ) {
            items(messages) { message ->
                ChatBubble(
                    message = message,
                    isCurrentUser = message.senderId == currentUser?.uid
                )
            }
        }

        // Message Input Section
        MessageInput(onSend = { text ->
            val userId = currentUser?.uid ?: return@MessageInput
            viewModel.sendMessageAndUpdateChatList(receiverUserId, text)
        })
    }
}

@Composable
fun MessageInput(onSend: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            placeholder = { Text("Type a message") },
            modifier = Modifier.weight(1f).padding(8.dp),
            singleLine = true
        )
        IconButton(
            onClick = {
                if (messageText.isNotEmpty()) {
                    onSend(messageText)
                    messageText = ""
                }
            }
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
        }
    }
}

@Composable
fun ChatBubble(message: Message, isCurrentUser: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Text(
            text = message.message,
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp),
            color = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
    }
}



 */

/*new
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavHostController,
    channelId: String,
    channelName: String,
    chatViewModel: ChatViewModel // Assuming you already have this
) {
    val messages by chatViewModel.getMessages(channelId).collectAsState(initial = emptyList())
    val currentMessage = remember { mutableStateOf("") }
    val currentUserId = Firebase.auth.currentUser?.uid

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = channelName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    reverseLayout = true // Latest message at the bottom
                ) {
                    items(messages) { message ->
                        ChatBubble(message = message)
                    }
                }
                // Input field for typing a new message
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = currentMessage.value,
                        onValueChange = { currentMessage.value = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Type a message...") }
                    )
                    IconButton(
                        onClick = {
                            if (currentMessage.value.isNotEmpty()) {
                                chatViewModel.sendMessage(
                                    channelId = channelId,
                                    senderId = currentUserId ?: "",
                                    message = currentMessage.value
                                )
                                currentMessage.value = ""
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Send")
                    }
                }
            }
        }
    )
}

@Composable
fun ChatBubble(message: Message) {
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val bubbleColor = if (isCurrentUser) Color.Blue else Color.Gray

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.align(
                if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(color = bubbleColor, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                if (message.imageUrl != null) {
                    AsyncImage(
                        model = message.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(150.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(text = message.message ?: "", color = Color.White)
                }
            }
        }

        // Message Timestamp
        Text(
            text = message.timestamp?.let { formatTimestamp(it) } ?: "",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(
                if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
            ).padding(4.dp)
        )
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}




 */

/*old
@Composable
fun ChatScreen(navController: NavController, channelId: String, channelName: String) {
    // Create the ViewModel instance manually without Hilt
    // Create the ViewModel manually if needed, or use viewModel() for default

    //  val viewModel: ChatViewModel = viewModel()  // Using ViewModelProvider
    val context = LocalContext.current
    //chnges by hilt
    val viewModel: ChatViewModel = remember { ChatViewModel(context) }
    val chooserDialog = remember { mutableStateOf(false) }
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    val cameraImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri.value?.let {
                viewModel.sendImageMessage(it, channelId)
            }
        }
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.sendImageMessage(it, channelId) }
    }

    fun createImageUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = ContextCompat.getExternalFilesDirs(
            context, Environment.DIRECTORY_PICTURES
        ).first()
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
                cameraImageUri.value = Uri.fromFile(this)
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraImageLauncher.launch(createImageUri())
        }
    }

    Scaffold(containerColor = Color.Black) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LaunchedEffect(Unit) {
                viewModel.listenForMessages(channelId)
            }

            val messages by viewModel.message.collectAsState()

            ChatMessages(
                messages = messages,
                onSendMessage = { message -> viewModel.sendMessage(channelId, message) },
                onImageClicked = { chooserDialog.value = true },
                channelName = channelName,
                channelID = channelId
            )
        }

        if (chooserDialog.value) {
            ContentSelectionDialog(
                onCameraSelected = {
                    chooserDialog.value = false
                    if (ContextCompat.checkSelfPermission(
                            context, Manifest.permission.CAMERA
                        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraImageLauncher.launch(createImageUri())
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                onGallerySelected = {
                    chooserDialog.value = false
                    imageLauncher.launch("image/*")
                }
            )
        }
    }
}

@Composable
fun ContentSelectionDialog(onCameraSelected: () -> Unit, onGallerySelected: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = onCameraSelected) { Text(text = "Camera") }
        },
        dismissButton = {
            TextButton(onClick = onGallerySelected) { Text(text = "Gallery") }
        },
        title = { Text(text = "Select Source") },
        text = { Text(text = "Would you like to pick an image from the gallery or use the camera?") }
    )
}

@Composable
fun ChatMessages(
    channelName: String,
    channelID: String,
    messages: List<Message>,
    onSendMessage: (String) -> Unit,
    onImageClicked: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val msg = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                ChatBubble(message = message)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onImageClicked) {
                Image(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Attach Image"
                )
            }
            TextField(
                value = msg.value,
                onValueChange = { msg.value = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(text = "Type a message...") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
            IconButton(onClick = {
                onSendMessage(msg.value)
                msg.value = ""
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Send Message"
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val bubbleColor = if (isCurrentUser) Color.Blue else Color.Gray

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.align(
                if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(color = bubbleColor, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                if (message.imageUrl != null) {
                    AsyncImage(
                        model = message.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(150.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(text = message.message ?: "", color = Color.White)
                }
            }
        }
    }
}



 */

/////



/*
//Jetpack Compose: UI banane ke liye use hota hai. Ye declarative hai, matlab UI ka code seedha screen ke behavior ko define karta hai.
//Chat Screen: Messages display karna, messages bhejna, aur images (camera ya gallery) attach karne ke options provide karta hai.
//Hilt: Dependency Injection ke liye use hota hai. ChatViewModel ko inject karne ke liye hiltViewModel() ka use kiya gaya hai.
//Image Handling: Camera aur gallery ke liye alag-alag launchers hain. Images ko Firebase ya backend pe bhejne ka setup hai.
//Permission Management: Camera ka permission nahi hone par runtime permission handle ki gayi hai.



//  //rules
//    //[upper tak kelav image uplod ka code hai
//    // //image cemar ya gallry
//    // //cemra to uri to japg
//    // // data send view to firebase]



@Composable
fun ChatScreen(navController: NavController, channelId: String, channelName: String) {
    val viewModel: ChatViewModel = hiltViewModel()
    val context = LocalContext.current

    //chooserDialog:
    //Ek dialog dikhane ke liye state hai (true/false). User ko gallery ya camera ka option dikhata hai.
    //cameraImageUri:
    //Camera se jo photo aayega uska URI store karega.
    val chooserDialog = remember { mutableStateOf(false) }
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    //ActivityResultContracts.TakePicture():
    //Camera open karne ke liye use hota hai.
    //cameraImageUri:
    //Agar photo successfully click ho gaya, to sendImageMessage function call hota hai, jo image Firebase ya backend pe bhejega.

    //{rul1 agar daolog box me user ne cemra choose kiya to
    // cemara open hoga or picther lene ke bad}
    val cameraImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {

            //1.1.1agra user ne cemra se photo li to usse pahle iamge uri se jpg me chnage hogi fun niche
            cameraImageUri.value?.let {
                viewModel.sendImageMessage(it, channelId)
                //1.1.3 yaha se imgae ko viewmodel me snedimage fun ko pass kar denge
            }
        }
    }

    //ActivityResultContracts.GetContent():
    //Gallery open karne ka contract. User ek image select kar sakta hai.
    //sendImageMessage:
    //Image ka URI pass karke backend pe bheja jata hai.

    //{rule 1.2 agar user ne galry chose ki to ye eopn hoga or vahi view model me sma e fun epen hoga}
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.sendImageMessage(it, channelId) }
    }

    //File Creation:
    //Camera image save karne ke liye ek unique file create hoti hai.
    //timeStamp unique naam deta hai.
    //FileProvider:
    //Temporary file ka URI return karta hai jo camera me use hota hai.

    //1.1.2 yaha iamge ko jpge me change kiya
    fun createImageUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = ContextCompat.getExternalFilesDirs(
            context, Environment.DIRECTORY_PICTURES
        ).first()
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
                cameraImageUri.value = Uri.fromFile(this)
            }
        )
    }

    //Permission Check:
    //Agar camera permission granted hai, to camera open hota hai.
    //Nahi to permission request karta hai.

    //rule user ne cemra khola to yaah se permision mangege
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraImageLauncher.launch(createImageUri())
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    //rule ab chat screen ka code hai

    Scaffold(containerColor = Color.Black) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            //rule 4.5[yaha viewmodel se data fetch ho rha hai yani jo msaage firebase me add huae vo realtime me featc ho rhe hai ]
            //[Messages: rule 2]
            //LaunchedEffect ke through messages listen karte hain.
            //yani real-time messages fetch karte hain aur UI update hota hai.
            // yaha viewmodel se data fetch ho rha hai
            LaunchedEffect(Unit) {
                viewModel.listenForMessages(channelId)
            }

            //4.6 uske massage var me dala

            val messages by viewModel.message.collectAsState()
//rule

            //3.3 vi image yaha aaye jo chooserDialog.value = true yaha gai
            //4.3 vi  massage yaha aaye jo view model me gya message -> viewModel.sendMessage(channelId, message)

            //4.6 or yaha message var em dal diya
            ChatMessages(
                messages = messages,
                onSendMessage = { message -> viewModel.sendMessage(channelId, message) },
                onImageClicked = { chooserDialog.value = true },
                channelName = channelName,
                channelID = channelId
            )
        }

//[ye hai ki jab image uplode karn button pe clik kare to ek dailog box oprn ]
        //chooserDialog.value:
        //Ek MutableState<Boolean> hai.
        //Agar true hai to dialog dikhta hai, aur false hai to hide ho jata hai.
        //ContentSelectionDialog:
        //Ek composable dialog hai jo user ko Camera aur Gallery ke options deta hai
        if (chooserDialog.value) {
            ContentSelectionDialog(
                onCameraSelected = {
                    chooserDialog.value = false
                    if (ContextCompat.checkSelfPermission(
                            context, Manifest.permission.CAMERA
                        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraImageLauncher.launch(createImageUri())
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                onGallerySelected = {
                    chooserDialog.value = false
                    imageLauncher.launch("image/*")
                }
            )
        }
    }
}


//AlertDialog:
//User ke saamne ek prompt show karta hai jisme 2 actions hote hain:
//Camera Button (confirmButton): onCameraSelected call hota hai.
//Gallery Button (dismissButton): onGallerySelected call hota hai.
//Title: "Select Source".
//Message: "Gallery ya Camera ka use karna chahenge?"


//Explanation:
//
//Dialog Band Karna:
//chooserDialog.value = false dialog ko hide karta hai.
//Permission Check:
//ContextCompat.checkSelfPermission CAMERA permission check karta hai.
//Agar permission di gayi hai:
//cameraImageLauncher.launch(createImageUri()) call hota hai, aur camera khulta hai.
//Agar permission nahi hai:
//permissionLauncher.launch se permission request hoti hai.


//Dialog Band Karna:
//chooserDialog.value = false dialog ko hide karta hai.
//Gallery Launch:
//imageLauncher.launch("image/*") device ki gallery open karta hai image select karne ke liye.

@Composable
fun ContentSelectionDialog(onCameraSelected: () -> Unit, onGallerySelected: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = onCameraSelected) { Text(text = "Camera") }
        },
        dismissButton = {
            TextButton(onClick = onGallerySelected) { Text(text = "Gallery") }
        },
        title = { Text(text = "Select Source") },
        text = { Text(text = "Would you like to pick an image from the gallery or use the camera?") }
    )
}


////////////////////////////////////////////////////////////////////////////////////////////////////

//Messages ko dikhane aur bhejne ka kaam karta hai.
//onSendMessage function text message bhejta hai.
//onImageClicked dialog dikhata hai.

//chart screen ka ui
@Composable

//rule 3.2 yaha image vale fun ne image yaha bheji jo upper gaye
//rule 4.2 yaha massage vale fun ne massage yaha bheji jo upper gaye

//gumkar 4.7 message yaha hi aagya
fun ChatMessages(
    channelName: String,
    channelID: String,
    messages: List<Message>,
    onSendMessage: (String) -> Unit,
    onImageClicked: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val msg = remember { mutableStateOf("") }

    // upper chat show hoge
    // Har message ko ChatBubble ko pass karenge vo fir dekga ki massage ko left ya right me rakhana hai

    //4.8 us meaage ko chat bubble me pass karenge
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                ChatBubble(message = message)
            }
        }
        //fir niche text fieal or button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image button: Yeh button image attach karne ke liye hai
            /////////             //on rule3[ image fun call ] uper
            IconButton(onClick = onImageClicked) {
                Image(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Attach Image"
                )
            }
            TextField(
                value = msg.value,// Jo user likh raha hai wo yahan save hota hai
                onValueChange = { msg.value = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(text = "Type a message...") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })// Keyboard band hota hai "Done" pe
            )
            IconButton(onClick = {
                ///////////////on rule4[onSendMessage] uper
                ////on sendmassage fun call jo bhi kikha vo gya
                onSendMessage(msg.value)
                msg.value = ""
            }) {
                //send image ka icon
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Message bhejne ka function call hota hai
                    contentDescription = "Send Message"  // TextField ko clear kar deta hai
                )
            }
        }
    }
}


//Apne messages blue color me aur doosre messages gray color me dikhte hain.
//Agar image URL hai, to image bubble me dikhayi jati hai.


//4.8 yaha message ko chek karenge ki left me rakhna hai ya right me rakhna hai
//yani ye message login user ne likha to isCurrentUser hoga or vo buke hokar right me aajayega nhi to left me
@Composable
fun ChatBubble(message: Message) {
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val bubbleColor = if (isCurrentUser) Color.Blue else Color.Gray

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.align(
                if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(color = bubbleColor, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                if (message.imageUrl != null) {
                    AsyncImage(
                        model = message.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(150.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(text = message.message ?: "", color = Color.White)
                }
            }
        }
    }
}



 */