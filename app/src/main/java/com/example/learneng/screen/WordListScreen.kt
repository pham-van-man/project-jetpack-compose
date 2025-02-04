package com.example.learneng.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.learneng.model.Word
import com.example.learneng.viewModel.WordViewModel

@Composable
fun WordListScreen(
    wordViewModel: WordViewModel,
    snackBarHostState: SnackbarHostState,
    wordsChanged: (List<Word>) -> Unit
) {
    var q by rememberSaveable { mutableStateOf("") }
    val words by wordViewModel.words.observeAsState(emptyList())
    val errorMessage by wordViewModel.errorMessage.observeAsState(null)
    val isLoading by wordViewModel.isLoading.observeAsState(false)
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    LaunchedEffect(Unit) {
        wordViewModel.getWords()
    }
    LaunchedEffect(words) {
        wordsChanged(words)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = q,
            onValueChange = {
                q = it
                wordViewModel.getWords(it)
            },
            label = { Text("Tìm kiếm...") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                errorMessage != null -> {
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = errorMessage ?: "Lỗi lấy dữ liệu", color = Color.Red)
                        Text(
                            text = " Tải lại",
                            modifier = Modifier.clickable { wordViewModel.getWords() })
                    }
                }

                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                words.isNotEmpty() -> {
                    LazyColumn(state = listState, modifier = Modifier.fillMaxWidth()) {
                        items(words) { word ->
                            WordItem(
                                word = word,
                                wordViewModel = wordViewModel,
                                snackBarHostState = snackBarHostState,
                                scope = scope
                            )
                        }
                    }
                }

                else -> {
                    Text(
                        text = "Chưa có dữ liệu",
                        modifier = Modifier.align(Alignment.TopStart)
                    )
                }
            }
        }
    }
}