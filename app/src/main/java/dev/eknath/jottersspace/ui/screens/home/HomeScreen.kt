package dev.eknath.jottersspace.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import dev.eknath.jottersspace.R
import dev.eknath.jottersspace.entities.JotNote
import dev.eknath.jottersspace.ui.components.DefaultTopAppBar
import dev.eknath.jottersspace.zCatalystSDK.ZAuthSDK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    name: String,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val zApiSDK = ZAuthSDK.zApiSDK
    val jots = remember { mutableStateOf(emptyList<JotNote>()) }

    var title by remember { mutableStateOf(TextFieldValue()) }
    var note by remember { mutableStateOf(TextFieldValue()) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                titleStringRes = R.string.home,
                isLoading = false
            )

        }) {

        Surface(modifier = Modifier.padding(it)) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text("Hello $name")

                TextField(value = title, onValueChange = { title = it })
                TextField(value = note, onValueChange = { note = it })
                Button(
                    onClick = {
                        scope.launch {
                            zApiSDK.createNewJot(
                                data = JotNote(title = title.text, note = note.text),
                                onSuccess = {
                                    scope.launch {
                                        zApiSDK.getBulkJots(
                                            onSuccess = {
                                                Log.e("Test", "SSSS: ${it.data.size}")
                                                jots.value = it.data
                                            },
                                            onFailure = {
                                                Log.e("Test", "FFFF")
                                                jots.value = emptyList()
                                            }
                                        )
                                    }
                                },
                                onFailure = {
                                    Log.e("Test", "FFFF")
                                }
                            )
                        }
                    }
                ) {
                    Text("Create Note")
                }

                LazyColumn {
                    items(jots.value) {
                        Text("Note:  ${it.title} ID: ${it.id}")
                    }
                }
            }
        }
    }



    LaunchedEffect(Unit) {
        scope.launch {
            zApiSDK.getBulkJots(
                onSuccess = {
                    Log.e("Test", "SSSS: ${it.data.size}")
                    jots.value = it.data
                },
                onFailure = {
                    Log.e("Test", "FFFF")
                    jots.value = emptyList()
                }
            )
        }
    }
}