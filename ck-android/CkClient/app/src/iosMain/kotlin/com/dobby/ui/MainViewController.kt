package com.dobby.ui

import androidx.compose.ui.window.ComposeUIViewController
import com.dobby.feature.main.presentation.MainViewModel
import com.dobby.navigation.App

fun MainViewController(
    mainViewModel: MainViewModel
) = ComposeUIViewController {  App(mainViewModel = mainViewModel) }
