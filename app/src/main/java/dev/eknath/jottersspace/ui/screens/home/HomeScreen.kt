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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.eknath.jottersspace.R
import dev.eknath.jottersspace.entities.JotNote
import dev.eknath.jottersspace.ui.components.DefaultIconButton
import dev.eknath.jottersspace.ui.components.DefaultTopAppBar
import dev.eknath.jottersspace.ui.navigation.AppNavSpec
import dev.eknath.jottersspace.ui.screens.gettingstarted.Spacer
import dev.eknath.jottersspace.ui.screens.note.NoteContent
import dev.eknath.jottersspace.zCatalystSDK.ZAuthSDK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    shortCutCode: Int,
    name: String,
    scope: CoroutineScope = rememberCoroutineScope(),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val authSDK = ZAuthSDK
    val zApiSDK = ZAuthSDK.zApiSDK
    val jots = remember { mutableStateOf(emptyList<JotNote>()) }
    var currentJot: JotNote? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(false) }
    var reFetchJot by remember { mutableStateOf(false) }

    //i want this to be first even before the launched effect or other UI
    if (viewModel.shortCutExecutionCode == -1 && shortCutCode == 1) {
        currentJot = JotNote(
            title = "",
            note = ""
        )
        viewModel.shortCutExecutionCode = 1
    }

    suspend fun createJot(jot: JotNote): JotNote {
        return suspendCoroutine { cont ->
            zApiSDK.createNewJot(
                data = JotNote(
                    title = jot.title,
                    note = jot.note
                ),
                onSuccess = {
                    reFetchJot = true
                    cont.resume(it)
                },
                onFailure = {
                    cont.resume(jot)
                    Log.e("Test", "FFFF")
                }
            )
        }
    }

    suspend fun updateJot(jot: JotNote): JotNote {
        return suspendCoroutine { cont ->
            zApiSDK.updateJot(
                data = jot,
                onSuccess = {
                    reFetchJot = true
                    cont.resume(it)
                },
                onFailure = {
                    cont.resume(jot)
                    Log.e("Test", "FFFF")
                }
            )
        }
    }

    fun handleOnSave(jot: JotNote): Deferred<JotNote> {
        return scope.async {
            if (jot.id == 0L) {
                createJot(jot)
            } else {
                updateJot(jot)
            }
        }
    }

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                titleStringRes = R.string.notes,
                isLoading = false,
                actions = {
                    DefaultIconButton(
                        iconRes = R.drawable.ic_logout,
                        onClick = {
                            scope.launch {
                                authSDK.logOutUser(
                                    onSuccess = {
                                        navController.navigate(AppNavSpec.AppSwitcher)
                                    },
                                    onError = {}
                                )
                            }
                        }
                    )
                }
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
                onSaveChange = {
                    handleOnSave(it)
                },
                onBackPressed = {
                    currentJot = null
                },
            )
    }

    LaunchedEffect(key1 = Unit, key2 = reFetchJot) {
        if (reFetchJot || jots.value.isEmpty())
            scope.launch {
                zApiSDK.getBulkJots(
                    onSuccess = {
                        Log.e("Test", "SSSS: ${it.data.size}")
                        jots.value = it.data
                        reFetchJot = false
                    },
                    onFailure = {
                        Log.e("Test", "FFFF")
                        jots.value = emptyList()
                        reFetchJot = false
                    }
                )
            }
    }
}

@Composable
fun NoteListItem(
    note: JotNote,
    modifier: Modifier = Modifier,
    onClick: (JotNote) -> Unit = {}
) {
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


