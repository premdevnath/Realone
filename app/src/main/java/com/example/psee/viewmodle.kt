package com.example.psee

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.psee.dataclass.userdata
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class viewmodle: ViewModel() {
    var auth : FirebaseAuth= FirebaseAuth.getInstance()
    var dataref= Firebase.database.reference


    fun loginb(email: String, password: String, navController: NavHostController, context: Context){
               if (email.isNotEmpty() && password.isNotEmpty())
        {
                   auth.signInWithEmailAndPassword(email,password)
                       .addOnCompleteListener{task->
                           if (task.isSuccessful)
                           {
                               //wornng code ek screen se duskre ke liye
                               navController.navigate("home")
                               Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                           }
                           else
                           {
                               Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                           }

                       }
        }
        else
               {
                   Toast.makeText(context, "fill all fields", Toast.LENGTH_SHORT).show()
               }
    }
    fun singupb(name:String,email: String, password: String, navController: NavHostController, context: Context)

    {
            if (name.isNotEmpty()&&email.isNotEmpty() && password.isNotEmpty())
            {
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener{
                        task->
                        if (task.isSuccessful)
                        {

                            saveuser(auth.currentUser!!.uid,dataref,name,email,password)
                            navController.navigate("login")
                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                        }
                        else
                        {
                            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        else
            {
                Toast.makeText(context, "fill all fields", Toast.LENGTH_SHORT).show()
            }
    }
    //


    fun loginUser2(email: String, password: String, navController: NavHostController,context: Context) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //aise login se home ya ek screen se dusri par lane ke liye ye code hota hai
                    //yani login se home par
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    Log.e("LoginError", task.exception?.message ?: "Error")
                }
            }
        } else {
            Log.e("Validation", "Please fill all fields")
        }
    }

    fun singupb2(name: String, email: String, password: String, navController: NavHostController, context: Context) {
        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            if (!isEmailValid(email)) {
                Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uid = auth.currentUser?.uid
                            if (uid != null) {
                                saveuser(uid, dataref, name, email, password)
                            }
                            Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, task.exception?.localizedMessage ?: "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                        /*
                        if (task.isSuccessful) {
                            val uid = auth.currentUser?.uid
                            if (uid != null) {
                                saveuser(uid, dataref, name, email, password)
                            }
                            /*
                            auth.currentUser?.uid?.let { uid ->
                                saveuser(uid, dataref, name, email, password)

                             */
                                /* agar ye to ya ye
                                 val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@addOnCompleteListener

                                 // Save user data in Firebase
                                     saveUser(uid, dataref, name, email, password)
                                */
                            }
                            Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, task.exception?.localizedMessage ?: "Error occurred", Toast.LENGTH_SHORT).show()
                        }

                         */
                    }
            }
        } else {
            Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

}

fun saveuser(
    uid: String,
    reference: DatabaseReference,
    name: String,
    email: String,
    password: String
) {
    val user = mapOf(
        "name" to name,
        "email" to email
    )

    reference.child("users").child(uid).setValue(user)
        .addOnSuccessListener {
            println("User saved successfully!")
        }
        .addOnFailureListener { e ->
            println("Error saving user: ${e.message}")
        }
}


/*old

private fun viewmodle.saveuser(
    uid: String,
    reference: DatabaseReference,
    name: String,
    email: String,
    password: String
) {
    uid.let {
        val user=userdata(name,email,password)
        //chnages
        reference.child("users").child(uid).setValue(user)

    }
}


 */
fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}