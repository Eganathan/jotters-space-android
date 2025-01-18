package dev.eknath.jottersspace.ui.screens.note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.eknath.jottersspace.R
import dev.eknath.jottersspace.entities.JotNote
import dev.eknath.jottersspace.ui.components.DefaultBackButton
import dev.eknath.jottersspace.ui.components.DefaultIconButton
import dev.eknath.jottersspace.ui.components.DefaultTopAppBar
import dev.eknath.jottersspace.ui.screens.gettingstarted.Spacer
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun NoteContent(
    modifier: Modifier = Modifier,
    jotNote: JotNote,
    onSaveChange: (JotNote) -> Deferred<JotNote>,
    onBackPressed: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val updateJob: MutableState<Job?> = remember { mutableStateOf(null) }
    val createJob: MutableState<Job?> = remember { mutableStateOf(null) }
    var latestJot by remember { mutableStateOf(jotNote) }
    var titleTextField by remember { mutableStateOf(TextFieldValue(jotNote.title)) }
    var noteTextField by remember { mutableStateOf(TextFieldValue(jotNote.note)) }
    val focusRequester = remember { androidx.compose.ui.focus.FocusRequester() }
    val tfColor = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
    )

    fun onSave() {
        val call: () -> Job = {
            scope.launch {
                latestJot = onSaveChange(
                    latestJot.copy(
                        note = noteTextField.text.trimIndent(),
                        title = titleTextField.text.trimIndent()
                    )
                ).await()
                createJob.value = null
            }
        }
        if (createJob.value == null) {
            if (latestJot.id == 0L) {
                if (createJob.value == null || createJob.value?.isActive == false) {
                    createJob.value = call()
                }
            } else {
                updateJob.value?.cancel()
                updateJob.value = call()
            }
        }
    }

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                titleStringRes = R.string.note,
                isLoading = false,
                navigation = {
                    DefaultBackButton(onBackPressed)
                },
                actions = {
                    DefaultIconButton(
                        iconRes = R.drawable.ic_options,
                        onClick = {}
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .imePadding(),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(height = 15.dp)
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                value = titleTextField,
                onValueChange = {
                    titleTextField = it
                    onSave()
                },
                placeholder = {
                    Text(
                        stringResource(id = R.string.note_title_placeholder),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.alpha(0.5f)
                    )
                },
                textStyle = MaterialTheme.typography.titleLarge,
                colors = tfColor.copy(
                    focusedIndicatorColor = MaterialTheme.colorScheme.background,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.background
                )
            )

            TextField(
                modifier = Modifier
                    .offset(y = (-22).dp)
                    .fillMaxSize()
                    .focusRequester(focusRequester),
                value = noteTextField,
                onValueChange = {
                    noteTextField = it
                    onSave()
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.note_content_placeholder),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = tfColor
            )
        }

        BackHandler(enabled = true, onBack = onBackPressed)
    }

//    AnimatedVisibility(visible = confirmationDialog.value) {
//        ConfirmationDialog(
//            title = "Delete Note",
//            description = "Are you sure you want to delete note?",
//            onDismiss = {
//                confirmationDialog.value = false
//            },
//            onConfirm = {
//                confirmationDialog.value = false
//                editorState.viewModel.selectedNoteId.value?.let {
//                    editorState.deleteNote(it)
//                    onBackPressed()
//                }
//
//            },
//            confirmText = "Delete"
//        )
//    }

    LaunchedEffect(
        key1 = Unit,
        block = {
            latestJot = jotNote
            if (jotNote.id == 0L)
                focusRequester.requestFocus()
        })

    DisposableEffect(key1 = Unit, effect = {
        onDispose { }
    })
}
