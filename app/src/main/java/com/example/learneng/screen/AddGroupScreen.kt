package com.example.learneng.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.learneng.model.Group
import com.example.learneng.viewModel.GroupViewModel
import kotlinx.coroutines.launch

@Composable
fun AddGroupScreen(groupViewModel: GroupViewModel, snackBarHostState: SnackbarHostState) {
    var nameGroup by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = nameGroup,
            onValueChange = { nameGroup = it },
            label = { Text("Name Group") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (nameGroup.isNotBlank()) {
                    val group = Group(name = nameGroup)
                    scope.launch {
                        val newGroup = groupViewModel.add(group)
                        nameGroup = ""
                        snackBarHostState.showSnackbar(
                            message = "Thêm nhóm mới thành công",
                            duration = SnackbarDuration.Short,
                            actionLabel = "Hoàn tác"
                        ).let { result ->
                            if (result == SnackbarResult.ActionPerformed) {
                                newGroup.let { groupViewModel.delete(it) }
                                nameGroup = group.name
                            }
                        }
                    }
                } else {
                    scope.launch {
                        snackBarHostState.showSnackbar(
                            message = "Thêm mới nhóm thất bại",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Thêm nhóm mới")
        }
    }
}