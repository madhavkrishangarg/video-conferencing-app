package com.madhav.mc_project

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.madhav.mc_project.ui.theme.MC_ProjectTheme
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madhav.mc_project.JitsiDAObject
import kotlinx.coroutines.launch

class Dashboard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = intent.getStringExtra("user")
        setContent {
            MC_ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Greeting(email)
                }
            }
        }
    }
}

class DashboardViewModel(private val jitsiDAO: JitsiDAObject) : ViewModel() {
    fun getUsersData(email: String) {
        viewModelScope.launch {
            val usersData = jitsiDAO.getUserData(email)
            // Handle the usersData as needed
        }
    }
}

@Composable
fun UserDataItem(userData: JitsiDB) {
    Text(text = "User Email: ${userData.email}, Meeting ID: ${userData.meeting_id}")
}

@Preview
@Composable
fun DashboardPreview() {
    val email = "madhav21333@iiitd.ac.in"
    Dashboard_Screen(email)
}

@Composable
fun Dashboard_Screen(email: String?) {
    val viewModel: DashboardViewModel = viewModel()
    if (email != null) {
        viewModel.getUsersData(email)
    }
    val usersDataState = remember { mutableStateListOf<JitsiDB>() }
    LazyColumn {
        items(usersDataState) { userData ->
            UserDataItem(userData)
        }
    }
}
