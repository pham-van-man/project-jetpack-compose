package com.example.learneng.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import com.example.learneng.model.GroupWithWord
import com.example.learneng.model.Word

@Composable
fun GroupItem(
    group: GroupWithWord,
    selectedItem: MutableIntState,
    index: Int,
    wordsChanged: (List<Word>) -> Unit
) {
    NavigationRailItem(
        icon = {
            Icon(
                Icons.Filled.List,
                contentDescription = group.group.name
            )
        },
        selected = selectedItem.intValue == index,
        onClick = {
            selectedItem.intValue = index
            wordsChanged(group.words)
        },
        label = { Text(group.group.name) }
    )
}