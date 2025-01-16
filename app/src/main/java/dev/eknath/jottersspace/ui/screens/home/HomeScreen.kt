package dev.eknath.jottersspace.ui.screens.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.eknath.jottersspace.R
import dev.eknath.jottersspace.entities.JotNote
import dev.eknath.jottersspace.ui.components.DefaultTopAppBar
import dev.eknath.jottersspace.ui.screens.gettingstarted.Spacer
import dev.eknath.jottersspace.ui.screens.note.NoteContent
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
    var currentJot: JotNote? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(false) }

    fun createJot(jot: JotNote) {
        scope.launch {
            zApiSDK.createNewJot(
                data = JotNote(
                    title = jot.title,
                    note = jot.note
                ),
                onSuccess = {
                    scope.launch {
                        zApiSDK.getBulkJots(
                            onSuccess = {
                                jots.value = it.data
                            },
                            onFailure = {
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

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                titleStringRes = R.string.notes,
                isLoading = false
            )

        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                onClick = {
                    currentJot = JotNote(title = "", note = "")
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {

        Surface(modifier = Modifier.padding(it)) {
            Column(modifier = Modifier.fillMaxSize()) {
//                Text("Hello $name")
//
//                TextField(value = title, onValueChange = { title = it })
//                TextField(value = note, onValueChange = { note = it })
//                Button(
//                    onClick = {
//                        scope.launch {
//                            zApiSDK.createNewJot(
//                                data = JotNote(title = title.text, note = note.text),
//                                onSuccess = {
//                                    scope.launch {
//                                        zApiSDK.getBulkJots(
//                                            onSuccess = {
//                                                Log.e("Test", "SSSS: ${it.data.size}")
//                                                jots.value = it.data
//                                            },
//                                            onFailure = {
//                                                Log.e("Test", "FFFF")
//                                                jots.value = emptyList()
//                                            }
//                                        )
//                                    }
//                                },
//                                onFailure = {
//                                    Log.e("Test", "FFFF")
//                                }
//                            )
//                        }
//                    }
//                ) {
//                    Text("Create Note")
//                }

                LazyColumn {
                    items(jots.value) {
                        NoteListItem(it) {
                            currentJot = it
                        }
                    }
                }
            }
        }
    }

    AnimatedVisibility(
        visible = currentJot != null,
        enter = scaleIn(
            animationSpec = tween(durationMillis = 500),
            transformOrigin = TransformOrigin(1f, 1f) // Bottom-right corner
        ) + fadeIn(),
        exit = scaleOut(
            animationSpec = tween(durationMillis = 500),
            transformOrigin = TransformOrigin(1f, 1f) // Bottom-right corner
        ) + fadeOut()
    ) {
        if (currentJot != null)
            NoteContent(
                jotNote = currentJot!!,
                onValueChange = {
                    createJot(it)
                },
                onBackPressed = {
                    currentJot = null
                },
            )
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

@Composable
fun NoteListItem(note: JotNote, modifier: Modifier = Modifier, onClick: (JotNote) -> Unit = {}) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(note) },
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(16.dp)
        ) {
            Text(
                text = note.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1
            )
            Spacer(height = 4.dp)
            Text(
                text = note.note,
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 3
            )
        }
    }
}


