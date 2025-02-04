package com.example.learneng.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.learneng.model.Word
import com.example.learneng.viewModel.WordViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WordItem(
    word: Word,
    wordViewModel: WordViewModel,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var editedKey by rememberSaveable { mutableStateOf(word.key) }
    var editedIpa by rememberSaveable { mutableStateOf(word.ipa) }
    var editedValue by rememberSaveable { mutableStateOf(word.value) }
    var editedType by rememberSaveable { mutableStateOf(word.type) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = word.key, style = MaterialTheme.typography.headlineSmall)
            Text(text = word.ipa, style = MaterialTheme.typography.bodySmall)
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = word.value, style = MaterialTheme.typography.headlineSmall)
            Text(text = word.type, style = MaterialTheme.typography.bodySmall)
        }
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(Icons.Default.MoreVert, contentDescription = "Lựa chọn")
        }
        Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                Text("Chỉnh sửa", modifier = Modifier
                    .padding(8.dp, 0.dp)
                    .clickable {
                        expanded = false
                        isEditing = true
                    })
                Text("Xóa", modifier = Modifier
                    .padding(8.dp, 0.dp)
                    .clickable {
                        scope.launch {
                            val oldWord = wordViewModel.delete(word)
                            snackBarHostState
                                .showSnackbar(
                                    message = "Xóa thành công",
                                    duration = SnackbarDuration.Short,
                                    actionLabel = "Hoàn tác"
                                )
                                .let { result ->
                                    if (result == SnackbarResult.ActionPerformed) {
                                        oldWord.let { wordViewModel.add(it) }
                                    }
                                }
                        }
                    }
                )
            }
        }
    }
    if (isEditing) {
        Dialog(onDismissRequest = { isEditing = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Chỉnh sửa từ",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    TextField(
                        value = editedKey,
                        onValueChange = { editedKey = it },
                        label = { Text("Key") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = editedIpa,
                        onValueChange = { editedIpa = it },
                        label = { Text("IPA") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = editedValue,
                        onValueChange = { editedValue = it },
                        label = { Text("Value") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = editedType,
                        onValueChange = { editedType = it },
                        label = { Text("Type") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = { isEditing = false }) {
                            Text("Hủy")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                isEditing = false
                                scope.launch {
                                    val oldWord = wordViewModel.update(
                                        word.copy(
                                            key = editedKey,
                                            ipa = editedIpa,
                                            value = editedValue,
                                            type = editedType
                                        )
                                    )
                                    snackBarHostState.showSnackbar(
                                        message = "Sửa thành công",
                                        duration = SnackbarDuration.Short,
                                        actionLabel = "Hoàn tác"
                                    ).let { result ->
                                        if (result == SnackbarResult.ActionPerformed) {
                                            oldWord?.let { wordViewModel.update(it) }
                                        }
                                    }
                                }

                            }
                        ) {
                            Text("Lưu")
                        }
                    }
                }
            }
        }
    }
}
