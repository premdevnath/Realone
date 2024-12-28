package com.example.psee.model

/*old

data class Message(
    val id: String = "",
    val senderId: String = "",
    val message: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val senderName: String = "",
    val senderImage: String? = null,
    val imageUrl: String? = null
)

 */


 /*
data class Message(
    val senderId: String? = null,
    val message: String? = null,
    val imageUrl: String? = null,
    val timestamp: Long = 0L
)

 */
 data class User(
     val uid: String = "",
     val name: String = "",
     val email: String = ""
 )


data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0L
)





/*old
data class Message(
    val id: String = "",
    val senderId: String = "",
    val message: String = "",
    val timestamp: Long = 0L,
    val senderName: String = "",
    val imageUrl: String? = null
)

pn
data class Message(
    val id: String = "",
    val senderId: String = "",
    val message: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val senderName: String = "",
    val senderImage: String? = null,
    val imageUrl: String? = null
)
 */