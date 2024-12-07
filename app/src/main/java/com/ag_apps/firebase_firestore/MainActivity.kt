package com.ag_apps.firebase_firestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.ag_apps.firebase_firestore.ui.theme.FirebaseFirestoreTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var user = User(
        name = "test 2",
        email = "test_2@gmail.com",
        age = 10
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val firestoreClient = FirestoreClient()

        setContent {
            FirebaseFirestoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Button(onClick = {
                            lifecycleScope.launch {
                                firestoreClient.insertUser(user).collect { id ->
                                    user = user.copy(id = id ?: "")
                                }
                            }
                        }) {
                            Text(text = "Insert")
                        }

                        Spacer(modifier = Modifier.height(50.dp))

                        Button(onClick = {
                            lifecycleScope.launch {

                                user = user.copy(
                                    name = "new name 1",
                                    age = 44
                                )

                                firestoreClient.updateUser(user).collect { result ->
                                    println("FirestoreClient: is updated = $result")
                                }
                            }
                        }) {
                            Text(text = "Update")
                        }

                        Spacer(modifier = Modifier.height(50.dp))

                        Button(onClick = {
                            lifecycleScope.launch {
                                firestoreClient.getUser(user.email).collect { result ->

                                    if (result != null) {
                                        user = result

                                        println("FirestoreClient: get user id = ${user.id}")
                                        println("FirestoreClient: get user name = ${user.name}")
                                        println("FirestoreClient: get user email = ${user.email}")
                                        println("FirestoreClient: get user age = ${user.age}")

                                    } else {
                                        println("FirestoreClient: did not get user")
                                    }
                                }
                            }
                        }) {
                            Text(text = "Get User")
                        }

                    }
                }
            }
        }
    }
}

