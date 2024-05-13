package com.madhav.mc_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madhav.mc_project.ui.theme.MC_ProjectTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

class Dashboard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = intent.getStringExtra("user")
        val context = this
        val JitsiDatabaseClass = JitsiDBClass.DatabaseBuilder.getInstance(context)
        val JitsiDatabaseAccessObject = JitsiDatabaseClass.JitsiDAO()
        println(JitsiDatabaseAccessObject::class.java)

        setContent {
            MC_ProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    DashboardScreen(email, JitsiDatabaseAccessObject)
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(email: String?, JitsiDatabaseAccessObject: JitsiDAObject) {
    val context = LocalContext.current
    var userData by remember { mutableStateOf<List<JitsiDB>>(emptyList()) }

    LaunchedEffect(email) {
        if (email != null) {
            withContext(Dispatchers.IO) {
                userData = JitsiDatabaseAccessObject.getUserData(email)
                println("User Data: $userData")
            }
        }
    }
    println("User Data (outside launched effect): $userData")

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Column {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "Welcome, $email!",
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Past Meetings:",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Column(
                modifier = Modifier
                    .padding(4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                LazyColumn(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .requiredHeight(300.dp)
                ) {
                    items(userData) { userData ->
                        Meeting_list(meetingID = userData.meeting_id)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { val intent = Intent(context, NewMeeting::class.java).apply {
                        putExtra("user", email)
                    }
                        context.startActivity(intent) },
                    modifier = Modifier
                        .width(260.dp)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6AB7FF))
                ) {
                    Text(
                        "Join/Create a new meeting",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
    }
}

@Composable
fun inflate_list(userData: List<JitsiDB>) {
    LazyColumn {
        items(userData) { userData ->
            Meeting_list(meetingID = userData.meeting_id)
        }
    }
}

@Composable
fun Meeting_list(meetingID: String) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clickable {
                try {
                    val url = URL("https://meet.jit.si")
                    val defaultOptions: JitsiMeetConferenceOptions =
                        JitsiMeetConferenceOptions.Builder().setServerURL(url)
                            .build()
                    JitsiMeet.setDefaultConferenceOptions(defaultOptions)

                    val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                        .setRoom(meetingID)
                        .build()
                    JitsiMeetActivity.launch(context, options)

                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }, shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            "Meeting ID: $meetingID", modifier = Modifier.padding(16.dp)
        )
    }
}


@Preview
@Composable
fun preview_inflate_list() {
    val userData = listOf<JitsiDB>(
        JitsiDB(1, "madhav@gmail.com", "meeting1"),
        JitsiDB(2, "madhav@gmail.com", "meeting2"),
        JitsiDB(3, "madhav@gmail.com", "meeting3"),
        JitsiDB(4, "madhav@gmail.com", "meeting4"),
        JitsiDB(5, "madhav@gmail.com", "meeting5"),
        JitsiDB(6, "madhav@gmail.com", "meeting6"),
        JitsiDB(7, "madhav@gmail.com", "meeting7")
    )
    val email = "madhav@gmail.com"
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Column {
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "Welcome, $email!",
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Past Meetings:",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Column(
                modifier = Modifier
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                LazyColumn(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .requiredHeight(300.dp)
                ) {
                    items(userData) { userData ->
                        Meeting_list(meetingID = userData.meeting_id)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { },
                    modifier = Modifier
                        .width(260.dp)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6AB7FF))
                ) {
                    Text(
                        "Create a new meeting",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

        }

    }
}
