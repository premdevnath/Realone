package com.example.psee

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//ye chat list massage ke liye haor or channel banene ke liye
//profile me massage button pe trigger hota hai
@Composable
fun chatlist(uid: String,oid: String,navController: NavController) {
    Log.d("Debug3", "Participants:${uid},${oid}")
    checkOrCreateChannel(uid, oid,navController)
}



fun checkOrCreateChannel(user1: String, user2: String, navController: NavController) {
    val db = FirebaseDatabase.getInstance().reference
    val channelId = if (user1 < user2) "$user1$user2" else "$user2$user1" // Unique ID
    Log.d("Debug31", "Participants:${channelId}")
    db.child("Channels").child(channelId).get().addOnSuccessListener { snapshot ->
        if (!snapshot.exists()) {
            // Agar channel exist nahi karta, to naya channel create karo
            Log.d("Debug32", "Participants:${user1},${user2},${channelId},${db}")
            createNewChannel(db, channelId, user1, user2,navController)
        } else {
            // Agar channel exist karta hai, to chat screen open karo
            Log.d("Debug33", "Participants:${channelId}")
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
        Log.d("Debug34", "Participants:${user1},${user2},${channelId},${db}")
        openChatScreen(channelId,navController)
    }
}
fun openChatScreen(channelId: String,navController: NavController)
{
    Log.d("Debug35", "Participants:${channelId}")
    navController.navigate("chat_screen/$channelId")
}
