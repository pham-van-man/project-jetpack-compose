package com.example.learneng.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learneng.model.Word
import com.example.learneng.viewModel.WordViewModel
import kotlinx.coroutines.launch

@Composable
fun AddWordScreen(wordViewModel: WordViewModel, snackBarHostState: SnackbarHostState) {
    var key by rememberSaveable { mutableStateOf("") }
    var ipa by rememberSaveable { mutableStateOf("") }
    var value by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = key,
            onValueChange = { key = it },
            label = { Text("Key") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = ipa,
            onValueChange = { ipa = it },
            label = { Text("IPA") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = { value = it },
            label = { Text("Value") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Type") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (key.isNotBlank() && value.isNotBlank()) {
                    val word = Word(key = key, ipa = ipa, value = value, type = type)
                    scope.launch {
                        val newWord = wordViewModel.add(word)
                        key = ""
                        ipa = ""
                        value = ""
                        type = ""
                        snackBarHostState.showSnackbar(
                            message = "Thêm từ mới thành công",
                            duration = SnackbarDuration.Short,
                            actionLabel = "Hoàn tác"
                        ).let { result ->
                            if (result == SnackbarResult.ActionPerformed) {
                                newWord.let { wordViewModel.delete(it) }
                                key = word.key
                                ipa = word.ipa
                                value = word.value
                                type = word.type
                            }
                        }
                    }
                } else {
                    scope.launch {
                        snackBarHostState.showSnackbar(
                            message = "Thêm mới từ thất bại",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Thêm từ mới")
        }
    }
}