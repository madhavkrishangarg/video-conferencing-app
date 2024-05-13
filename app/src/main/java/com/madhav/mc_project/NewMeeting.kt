package com.madhav.mc_project

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madhav.mc_project.ui.theme.MC_ProjectTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewMeeting : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = intent.getStringExtra("user")
        setContent {
            MC_ProjectTheme {
                New_meeting(user)
            }
        }
    }
}


@Composable
fun New_meeting(user: String?) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Column {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "Welcome, $user!",
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var meetingId by remember { mutableStateOf("") }

            TextField(
                value = meetingId,
                onValueChange = { meetingId = it },
                label = { Text("Enter Meeting ID") },
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Button(
                onClick = { createNewMeeting(meetingId, user, context) },
                modifier = Modifier
                    .width(160.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6AB7FF))

            ) {
                Text(
                    "Join Meeting", fontSize = 18.sp, textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4a")
@Composable
fun GreetingPreview() {
    val user = "Madhav"
    New_meeting(user)
}

fun createNewMeeting(meetingId: String, user: String?, context: Context) {
    //if user is not Guest, add the meeting to the database
    val JitsiDatabaseClass = JitsiDBClass.DatabaseBuilder.getInstance(context)
    val JitsiDatabaseAccessObject = JitsiDatabaseClass.JitsiDAO()
    if (user != "Guest") {
        GlobalScope.launch(Dispatchers.IO) {
            // Database operation to insert or update data
            JitsiDatabaseAccessObject.upsert(JitsiDB(null, user!!, meetingId))
        }
    }
}