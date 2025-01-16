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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.eknath.jottersspace.R
import dev.eknath.jottersspace.entities.JotNote
import dev.eknath.jottersspace.ui.components.DefaultTopAppBar

@Composable
fun NoteContent(
    modifier: Modifier = Modifier,
    jotNote: JotNote,
    onValueChange: (JotNote) -> Unit,
    onBackPressed: () -> Unit,
) {
    var titleTextField by remember { mutableStateOf(TextFieldValue(jotNote.title)) }
    var noteTextField by remember { mutableStateOf(TextFieldValue(jotNote.note)) }
    val focusRequester = remember { androidx.compose.ui.focus.FocusRequester() }

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                titleStringRes = R.string.empty_string,
                isLoading = false
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
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                value = titleTextField,
                onValueChange = {
                    titleTextField = it
                    onValueChange(jotNote.copy(title = it.text))
                },
                placeholder = {
                    Text(
                        stringResource(id = R.string.note_title_placeholder),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.alpha(0.5f)
                    )
                },
                textStyle = MaterialTheme.typography.titleLarge,
            )

            TextField(
                modifier = Modifier
                    .offset(y = (-22).dp)
                    .fillMaxSize()
                    .focusRequester(focusRequester),
                value = noteTextField,
                onValueChange = {
                    noteTextField = it
                    onValueChange(jotNote.copy(note = it.text))
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.note_content_placeholder),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                textStyle = MaterialTheme.typography.bodyLarge
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

    LaunchedEffect(key1 = Unit, block = {
        focusRequester.requestFocus()
    })
//
//    DisposableEffect(key1 = visibility.value, effect = {
//        onDispose { editorState.resetTextFieldAndSelection() }
//    })
}
