package com.madhav.mc_project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            LoginScreen(navController)
        }
    }
}


@Preview(device = "id:pixel_4a")
@Composable
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    LoginScreen(navController)
}


@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(120.dp))
        Text(
            text = "Video Conferencing App   MC Endsem Project",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.height(80.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image =
                    if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(image, "toggle password visibility")
                }
            },
            keyboardActions = KeyboardActions(onDone = { loginUser(email, password, context) }),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { loginUser(email, password, context) },
            modifier = Modifier
                .width(160.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6AB7FF))
        ) {
            Text(
                "Login",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { registerUser(email, password, context) },
            modifier = Modifier
                .width(160.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6AB7FF))
        ) {
            Text(
                "Register",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "or",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { continueAsGuest(context) },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6AB7FF))
        ) {
            Text(
                "Continue as Guest",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }


//        Button(
//            onClick = { navController.navigate("register") },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Register")
//        }

    }
}


fun loginUser(email: String, password: String, context: Context) {

    //if email and password are null, toast, please enter email and password
    if (email.isEmpty() || password.isEmpty()) {
        Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
        return
    }

    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                // Navigate to Dashboard.kt
                val intent = Intent(context, Dashboard::class.java).apply {
                    putExtra("user", email)
                }
                context.startActivity(intent)

            } else {
                Toast.makeText(
                    context,
                    "Login failed: ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
}

fun registerUser(email: String, password: String, context: Context) {

    if (email.isEmpty() || password.isEmpty()) {
        Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
        return
    }

    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                // Navigate to dashboard
            } else {
                Toast.makeText(
                    context,
                    "Registration failed: ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
}

fun continueAsGuest(context: Context) {
    val intent = Intent(context, NewMeeting::class.java).apply {
        putExtra("user", "madhav@gmail.com")
    }
    context.startActivity(intent)
}
