package com.dobby.feature.logging.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dobby.feature.logging.presentation.LogsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.currentKoinScope

@Preview
@Composable
fun LogScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    KoinContext {
        val viewModel: LogsViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsState()

        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(
                background = Color.White,
                onBackground = Color.Black
            )
        ) {
            Scaffold(
                topBar = { Toolbar(modifier, onBackClicked = { navController.popBackStack() }) },
                modifier = Modifier.fillMaxSize(),
                content = { innerPadding ->
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        Button(
                            onClick = { viewModel.copyLogsToClipBoard() },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Copy logs to clipboard")
                        }

                        Button(
                            onClick = { viewModel.clearLogs() },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            modifier = Modifier.fillMaxWidth()
                                .border(1.dp, Color.Black, shape = RoundedCornerShape(6.dp)),
                        ) {
                            Text("Clear Logs")
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            items(uiState.logMessages) { message ->
                                // some important logs contain this
                                val isBald = message.contains("!!!")
                                Text(
                                    fontWeight = FontWeight(if (isBald) 700 else 400),
                                    text = message,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
inline fun <reified T : ViewModel> koinViewModel(): T {
    val scope = currentKoinScope()
    return viewModel {
        scope.get<T>()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Toolbar(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Back",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = Color.Black
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        },
        modifier = modifier,
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.White
        )
    )
}