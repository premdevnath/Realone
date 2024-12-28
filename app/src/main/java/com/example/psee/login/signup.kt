package com.example.psee.login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import com.example.psee.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth


import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import androidx.navigation.NavHostController

import com.example.psee.viewmodle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



@Composable
fun signup(navController: NavHostController, newviewmodel: viewmodle) {
    //tetx field me email passwor dalne ke liye aise var banae hote hai
    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    // val authState = newviewmodel.authstate.observeAsState()
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
    val token = stringResource(id = R.string.client_id)
    val context = LocalContext.current

    //5.
    /*
    LaunchedEffect(authState.value) {
        when (authState) {
            is AuthState.Authentication -> {
                navController.navigate("home")
            }

            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState.value as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            else -> Unit
        }
    }

     */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1D1D)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo Image
        Image(
            painter = painterResource(R.drawable.home),
            contentDescription = "Spotify Logo",
            modifier = Modifier
                .size(120.dp) // Larger logo
                .padding(bottom = 20.dp)
        )

        // "Sign Up" Text
        Text(
            text = "Sign Up",
            fontSize = 32.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Name Field
        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Name", color = Color.Gray) },
            textStyle = TextStyle(color = Color.White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(300.dp)
                .height(80.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Email Field
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email", color = Color.Gray) },
            textStyle = TextStyle(color = Color.White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(300.dp)
                .height(80.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Password Field
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Password", color = Color.Gray) },
            textStyle = TextStyle(color = Color.White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(300.dp)
                .height(80.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Sign Up Button
        Button(
            onClick = { newviewmodel.singupb2(name, email, password, navController, context) },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(55.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF32DC3A))
        ) {
            Text(text = "Create Account", fontSize = 18.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(15.dp))
//or ke liye
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 10.dp)
                .padding(end = 10.dp)
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

        //google
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
                    text = "Already have an account?",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Login In",
                    color = Color(0xFF32DC3A),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate("login")
                    }
                )
            }

    }
}


    /* sahi
@Composable
fun signup(navController: NavHostController, newviewmodel: viewmodle) {
    //tetx field me email passwor dalne ke liye aise var banae hote hai
    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
   // val authState = newviewmodel.authstate.observeAsState()
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
    val token = stringResource(id = R.string.client_id)
    val context = LocalContext.current

    //5.
    /*
    LaunchedEffect(authState.value) {
        when (authState) {
            is AuthState.Authentication -> {
                navController.navigate("home")
            }

            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState.value as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            else -> Unit
        }
    }

     */


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1D1D)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(R.drawable.home),
            contentDescription = "loginimage",
            modifier = Modifier
                .size(80.dp)
                .padding(top = 1.dp),
            alignment = Alignment.TopCenter
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Sing Up",
            fontSize = 30.sp,
            color = Color.White,

            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
        )
        Spacer(modifier = Modifier.height(10.dp))
        //click

        OutlinedTextField(value = name, onValueChange = {
            name = it
        }, label = { Text(text = "name") },
            textStyle = TextStyle(color = Color.White), // Change text color here
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(300.dp)
                .height(80.dp)
        )


        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = email, onValueChange = {
                email = it
            },
            textStyle = TextStyle(color = Color.White), // Change text color here
            label = { Text(text = "Email") },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(300.dp)
                .height(80.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = password, onValueChange = {
            password = it
        }, label = { Text(text = "Password") },
            textStyle = TextStyle(color = Color.White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(300.dp)
                .height(80.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))


        // ClickableText(text = "Sign Up", onClick = {}) { }


        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                newviewmodel.singupb2(name, email, password, navController, context)
            }, modifier = Modifier

                .width(300.dp) // Adjust button width
                .height(80.dp) // Adjust button height
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF32DC3A)) // Adjust button color) {
        ) {
            Text(text = "Creat Accaount")

        }
        Spacer(modifier = Modifier.height(10.dp))
        //or ke liye
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 10.dp)
                .padding(end = 10.dp)
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

        //google
        Row(horizontalArrangement = Arrangement.Center) {
            //  if (user == null) {
            Spacer(modifier = Modifier.height(35.dp))
            ElevatedButton(
                onClick = {
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
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxSize()
                    .padding(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "",
                    modifier = Modifier.size(45.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    "Sign in via Google",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    letterSpacing = 0.1.em
                )
            }

            TextButton(onClick = { navController.navigate("login") }) {
                Text(text = "Not A Member? Register Now", color = Color(0xFFFDFDFD))
            }
        }
    }

     */





    //google singout p2
    @Composable
    fun rememberFirebaseAuthLauncher(
        onAuthComplete: (AuthResult) -> Unit,
        onAuthError: (ApiException) -> Unit,
        navController: NavController
    ): ManagedActivityResultLauncher<Intent, ActivityResult> {
        val scope = rememberCoroutineScope()
        return rememberLauncherForActivityResult(
            ActivityResultContracts
                .StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn
                .getSignedInAccountFromIntent(result.data)
            try {
                val account = task
                    .getResult(ApiException::class.java)!!
                Log.d("GoogleAuth", "account $account")
                val credential = GoogleAuthProvider
                    .getCredential(account.idToken!!, null)
                scope.launch {
                    val authResult = Firebase
                        .auth.signInWithCredential(credential).await()
                    onAuthComplete(authResult)
                    //google to home page
                    navController.navigate("home")
                }
            } catch (e: ApiException) {
                Log.d("GoogleAuth", e.toString())
                onAuthError(e)
            }
        }

    }
