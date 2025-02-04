package com.example.learneng

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learneng.model.Word
import com.example.learneng.screen.AddGroupScreen
import com.example.learneng.screen.AddWordScreen
import com.example.learneng.screen.GroupItem
import com.example.learneng.screen.PlayScreen
import com.example.learneng.screen.WordListScreen
import com.example.learneng.ui.theme.LearnEngTheme
import com.example.learneng.viewModel.GroupViewModel
import com.example.learneng.viewModel.WordViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val selectedItem = rememberSaveable { mutableIntStateOf(0) }
    val wordViewModel: WordViewModel = viewModel()
    val groupViewModel: GroupViewModel = viewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val words = remember { mutableStateOf(emptyList<Word>()) }
    val groups by groupViewModel.groups.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        groupViewModel.getGroups()
    }
    LearnEngTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },

            topBar = {
            },

            bottomBar = {
            },
            content = { paddingValues ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    NavigationRail(
                        modifier = Modifier.fillMaxHeight(),
                        header = {
                        },
                        content = {
                            NavigationRailItem(
                                icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                                selected = selectedItem.intValue == 0,
                                onClick = {
                                    selectedItem.intValue = 0
                                    navController.navigate("home")
                                },
                                label = { Text("Home") }
                            )
                            NavigationRailItem(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "Word"
                                    )
                                },
                                selected = selectedItem.intValue == 1,
                                onClick = {
                                    selectedItem.intValue = 1
                                    navController.navigate("addWord")
                                },
                                label = { Text("Word") }
                            )
                            NavigationRailItem(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "Group"
                                    )
                                },
                                selected = selectedItem.intValue == 2,
                                onClick = {
                                    selectedItem.intValue = 2
                                    navController.navigate("addGroup")
                                },
                                label = { Text("Group") }
                            )
                            PlayScreen(words.value)
                            groups.forEachIndexed { index, group ->
                                GroupItem(
                                    group = group,
                                    selectedItem = selectedItem,
                                    index = index + 4,
                                    wordsChanged = {
                                        words.value = it
                                    })
                            }
                            NavigationRailItem(
                                icon = {
                                    Icon(
                                        Icons.Filled.Settings,
                                        contentDescription = "Settings"
                                    )
                                },
                                selected = selectedItem.intValue == 3,
                                onClick = {
                                    selectedItem.intValue = 3
                                    navController.navigate("settings")
                                },
                                label = { Text("Settings") }
                            )
                        }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "home"
                        ) {
                            composable("home") {
                                WordListScreen(
                                    wordViewModel = wordViewModel,
                                    snackBarHostState = snackBarHostState
                                ) {
                                    words.value = it
                                }
                            }
                            composable("addWord") {
                                AddWordScreen(
                                    wordViewModel = wordViewModel,
                                    snackBarHostState = snackBarHostState
                                )
                            }
                            composable("addGroup") {
                                AddGroupScreen(
                                    groupViewModel = groupViewModel,
                                    snackBarHostState = snackBarHostState
                                )
                            }
                            composable("settings") {

                            }
                        }
                    }
                }
            }
        )
    }
}

