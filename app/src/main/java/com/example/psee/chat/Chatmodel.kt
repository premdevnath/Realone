package com.example.psee.chat
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.psee.model.Message
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import com.example.psee.R




class ChatModel : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val database = FirebaseDatabase.getInstance().reference
    //cpn
    fun sendMessage(channelID: String, senderId: String, text: String, context: Context) {
        if (text.isNotBlank()) {
            val messageId = database.child("Messages").child(channelID).push().key ?: return
            val message = Message(senderId, text, System.currentTimeMillis())
            database.child("Messages").child(channelID).child(messageId).setValue(message)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Update the Channels node to show activity
                        database.child("Channels").child(channelID).child("lastMessageTime")
                            .setValue(System.currentTimeMillis())
                        postNotificationToUsers(channelID, text, context)
                        Log.d("Debug41", "Participants: ${channelID},${task},${text}")
                    } else {
                        Log.e("SendMessage", "Failed to send message")
                        Log.e("Debug42", "Failed to send message")
                    }
                }
        }
    }

    /*
    fun sendMessage(channelID: String, senderId: String, text: String,context: Context) {
        if (text.isNotBlank()) {
            val messageId = database.child("Messages").child(channelID).push().key ?: return
            val message = Message(senderId, text, System.currentTimeMillis())
            database.child("Messages").child(channelID).child(messageId).setValue(message)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        postNotificationToUsers(channelID, text,context)
                    } else {
                        Log.e("SendMessage", "Failed to send message")
                    }
                }
        }
    }

     */


    private fun postNotificationToUsers(channelID: String, messageContent: String,context: Context) {
        val fcmUrl = "https://fcm.googleapis.com/v1/projects/notes-fbe4a/messages:send"
        val jsonBody = JSONObject().apply {
            put("message", JSONObject().apply {
                put("topic", "group_$channelID")
                put("notification", JSONObject().apply {
                    put("title", "New message in $channelID")
                    put("body", messageContent)
                })
            })
        }


        val requestBody = jsonBody.toString()

        val request = object : StringRequest(Method.POST, fcmUrl, Response.Listener {
            Log.d("ChatViewModel", "Notification sent successfully")
        }, Response.ErrorListener {
            Log.e("ChatViewModel", "Failed to send notification")
        }) {
            override fun getBody(): ByteArray = requestBody.toByteArray()

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer ${getAccessToken(context)}"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    fun subscribeForNotification(channelID: String) {
        FirebaseMessaging.getInstance().subscribeToTopic("group_$channelID")
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Debug37.1", "Participants: ${channelID}")
                    Log.d("ChatViewModel", "Subscribed to topic: group_$channelID")
                } else {
                    Log.d("Debug37.2", "Participants: ${channelID}")
                    Log.d("ChatViewModel", "Failed to subscribe to topic: group_$channelID")
                }
            }
    }


    fun listenToMessages(channelId: String) {
        Log.d("Debug36.1", "Participants: ${channelId}")
        database.child("Messages").child(channelId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                        //dikkat 1
                    viewModelScope.launch {
                        _messages.value = listOf(message) + _messages.value
                        Log.d("Debug36.2", "Participants: ${_messages}")
                    }

                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }







    private fun getAccessToken(context: Context): String {
        // Replace with actual token retrieval logic
       // return "your_access_token"
        val inputStream = context.resources.openRawResource(R.raw.chatter_key)
        val googleCreds = GoogleCredentials.fromStream(inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
        return googleCreds.refreshAccessToken().tokenValue
    }
}






