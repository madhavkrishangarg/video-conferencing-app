package com.madhav.mc_project

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madhav.mc_project.ui.theme.MC_ProjectTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
    inflate_list(userData = userData)
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
                Toast.makeText(context, "Meeting ID: $meetingID", Toast.LENGTH_SHORT).show()
            }, shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "Meeting ID: $meetingID", modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun preview_meeting_list() {
    Meeting_list("meeting1")
}

@Preview
@Composable
fun preview_inflate_list() {
    val userData = listOf<JitsiDB>(
        JitsiDB(1, "madhav@gmail.com", "meeting1"),
        JitsiDB(
            2, "madhav@gmail.com", "meeting2"
        ),
        JitsiDB(3, "madhav@gmail.com", "meeting3"),
        JitsiDB(4, "madhav@gmail.com", "meeting4"),
        JitsiDB(5, "madhav@gmail.com", "meeting5")
    )
    inflate_list(userData)
}

//class DashboardViewModel(application: Application, email: String?) : AndroidViewModel(application) {
//
//    private val jitsiDAO = JitsiDBClass.DatabaseBuilder.getInstance(application).JitsiDAO()
//
//
//    val usersDataState by lazy { jitsiDAO.getUserData(email).asLiveData() }
//    )
//}
//
//@Composable
//fun UserDataItem(userData: JitsiDB) {
//    Text(text = "User Email: ${userData.email}, Meeting ID: ${userData.meeting_id}")
//}
//
//@Preview
//@Composable
//fun DashboardPreview() {
//    val email = "madhav21333@iiitd.ac.in"
//    Dashboard_Screen(email)
//}

//@Composable
//fun Dashboard_Screen(email: String?) {
//    val viewModel: DashboardViewModel = viewModel()
//    if (email != null) {
//        viewModel.getUsersData(email)
//    }
//    val usersDataState = remember { mutableStateListOf<JitsiDB>() }
//    LazyColumn {
//        items(usersDataState) { userData ->
//            UserDataItem(userData)
//        }
//    }
//}
