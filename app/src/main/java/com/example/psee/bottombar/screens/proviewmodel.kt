package com.example.psee.bottombar.screens

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage

object FirebaseManager {
    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.getReference("users")
//new
fun fetchCurrentUser(callback: (User?, String?) -> Unit) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    if (currentUserId != null) {
        FirebaseDatabase.getInstance().getReference("users/$currentUserId")
            .get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                callback(user, null)
            }
            .addOnFailureListener { error ->
                callback(null, error.message)
            }
    } else {
        callback(null, "No logged-in user")
    }
}

    fun fetchUser(userId: String, callback: (User?, String?) -> Unit) {
        FirebaseDatabase.getInstance().getReference("users/$userId")
            .get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                callback(user, null)
            }
            .addOnFailureListener { error ->
                callback(null, error.message)
            }
    }

//end
    /*sahi
    // Fetch User Data
    fun fetchUser(userId: String, onComplete: (User?, String?) -> Unit) {
        userRef.child(userId).get().addOnSuccessListener { snapshot ->
            val user = snapshot.getValue(User::class.java)
            if (user != null) {
                onComplete(user, null) // User fetched successfully
            } else {
                onComplete(null, "User not found") // No user data
            }
        }.addOnFailureListener { exception ->
            onComplete(null, exception.localizedMessage ?: "Unknown error occurred")
        }
    }

     */

    // Save or Update User Data
    fun saveUser(user: User, onComplete: (Boolean, String?) -> Unit) {
        if (user.id.isBlank() || user.name.isBlank()) {
            onComplete(false, "Invalid user data. ID and name are required.")
            return
        }

        userRef.child(user.id).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(true, null) // Save successful
            } else {
                onComplete(false, task.exception?.localizedMessage ?: "Failed to save user data")
            }
        }
    }
    fun uploadProfileImage(userId: String, imageUri: Uri, callback: (String?, String?) -> Unit) {
        val storageRef = Firebase.storage.reference.child("profile_images/$userId.jpg")
        storageRef.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                storageRef.downloadUrl
            }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(task.result.toString(), null)
                } else {
                    callback(null, task.exception?.message)
                }
            }
    }
}


/*
object FirebaseManager {
    private val database = FirebaseDatabase.getInstance()
    private val userRef = database.getReference("users")

    // Fetch User Data
    fun fetchUser(userId: String, onComplete: (User?) -> Unit) {
        userRef.child(userId).get().addOnSuccessListener { snapshot ->
            val user = snapshot.getValue(User::class.java)
            onComplete(user)
        }.addOnFailureListener {
            onComplete(null)
        }
    }

    // Save or Update User Data
    fun saveUser(user: User, onComplete: (Boolean) -> Unit) {
        userRef.child(user.id).setValue(user).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }
}


 */