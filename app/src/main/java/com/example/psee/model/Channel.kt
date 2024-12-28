package com.example.psee.model



/*pn
data class Channel(
    val id: String = "",
    val name: String,
    val createdAt: Long = System.currentTimeMillis())



 */
data class Channel(
    val lastMessage: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val participants: List<String> = listOf() // List of UIDs of participants
)


