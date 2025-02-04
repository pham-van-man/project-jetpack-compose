package com.example.learneng.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.learneng.model.Word
import kotlinx.coroutines.delay

@Composable
fun PlayScreen(wordList: List<Word>) {
    val isDialogOpen = rememberSaveable { mutableStateOf(false) }
    val currentWordIndex = rememberSaveable { mutableIntStateOf(0) }
    val isKey = rememberSaveable { mutableStateOf(true) }
    val currentWord = remember { mutableStateOf<Word?>(null) }
    LaunchedEffect(isDialogOpen.value) {
        if (isDialogOpen.value) {
            currentWordIndex.intValue = 0
            while (currentWordIndex.intValue < wordList.size) {
                isKey.value = true
                currentWord.value = wordList[currentWordIndex.intValue]
                delay(500L)
                isKey.value = false
                delay(500L)
                currentWordIndex.intValue++
            }
        }
    }
    IconButton(
        onClick = { isDialogOpen.value = true },
        modifier = Modifier
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play"
            )
            Text(
                text = "Play",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
    if (isDialogOpen.value) {
        Dialog(onDismissRequest = { isDialogOpen.value = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = Color.DarkGray
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    currentWord.value?.let {
                        if (isKey.value) {
                            Text(
                                text = it.key,
                                style = MaterialTheme.typography.headlineLarge,
                            )
                            if (it.ipa.isNotBlank()) {
                                Text(
                                    text = it.ipa,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        } else {
                            Text(
                                text = it.value,
                                style = MaterialTheme.typography.headlineLarge,
                            )
                            if (it.type.isNotBlank()) {
                                Text(
                                    text = it.type,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}