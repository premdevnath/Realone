package com.example.psee.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement


import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.psee.viewmodle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import com.example.psee.R

import com.example.psee.R.string
import com.example.psee.R.drawable




@Composable
fun login(navController: NavHostController, newviewmodel: viewmodle) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    val currentUser = auth.currentUser

    /* hum direct is code ko spalsh me laga rahe hai
    // This effect will run once the composable is shown
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            // Navigate to "home" destination if the user is logged in
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

     */
    //   val authState = AuthViewModel.authstate.observeAsState()


    /*  LaunchedEffect(authState.value) {
          when (authState.value) {
              is AuthState.Authentication -> {
                  navController.navigate("home")
              }

              is AuthState.Error -> Toast.makeText(
                  context,
                  (authState.value as AuthState.Error).message,
                  Toast.LENGTH_SHORT
              )
                  .show()

              else -> Unit
          }

      }

     */


    val token = stringResource(id = string.client_id)

    //google singin
    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            user = result.user
        },
        onAuthError = {
            user = null
        },
        navController
    )


    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF1E1D1D)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(R.drawable.home),
            contentDescription = "loginimage",
            modifier = Modifier.size(150.dp)
                .padding(bottom = 100.dp),
            alignment = Alignment.TopCenter
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Sign In",
            fontSize = 30.sp,
            color = Color.White,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
        )
        Spacer(modifier = Modifier.height(10.dp))
        //click


        OutlinedTextField(
            value = email, onValueChange = {
                email = it
            },

            placeholder = { Text("Enter Email", color = Color.White) },
            label = { Text(text = "Email") },
            textStyle = TextStyle(color = Color.White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.width(300.dp)
                .height(80.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = password, onValueChange = {
            password = it
        }, label = { Text(text = "Password") },
            textStyle = TextStyle(color = Color.White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.width(300.dp)
                .height(80.dp)

        )

        Spacer(modifier = Modifier.height(10.dp))
        // ClickableText(text = "Sign Up", onClick = {}) { }


        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
               newviewmodel.loginUser2(email, password, navController,context)
            }, modifier = Modifier

                .width(300.dp) // Adjust button width
                .height(80.dp) // Adjust button height
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF32DC3A)) // Adjust button color) {
        ) {
            Text(text = "Login")

        }
        Spacer(modifier = Modifier.height(10.dp))
        //or ke liye
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 10.dp).padding(end = 10.dp)
        ) {
            Divider(modifier = Modifier.weight(1f), color = Color.Gray)
            Text(
                text = "Or",
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 14.sp
            )
            Divider(modifier = Modifier.weight(1f), color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.Center) {
            Image(painter = painterResource(id = R.drawable.google), contentDescription = "Google",
                modifier = Modifier
                    .size(50.dp)
                    .padding(start = 7.dp)
                    .clickable {
                        val gso =
                            GoogleSignInOptions
                                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(token)
                                .requestEmail()
                                .build()
                        val googleSignInClient = GoogleSignIn
                            .getClient(context, gso)
                        launcher
                            .launch(googleSignInClient.signInIntent)

                    })
        }
        // Clickable "Already have an account? Sign In"
        Row {
            Text(
                text = "Not A Member? Register Now",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Sign In",
                color = Color(0xFF32DC3A),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }

    }
}