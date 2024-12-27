package com.example.ck_client

import androidx.compose.ui.window.ComposeUIViewController
import com.dobby.main.presentation.MainViewModel
import com.dobby.navigation.App

fun MainViewController(
    mainViewModel: MainViewModel
) = ComposeUIViewController {  App(mainViewModel = mainViewModel) }
