package com.dobby.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dobby.logging.ui.LogScreen
import com.dobby.main.presentation.MainViewModel
import com.dobby.main.ui.DobbySocksScreen

@Composable
fun App(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "DobbySocksScreen") {
        composable("DobbySocksScreen") {
            DobbySocksScreen(
                modifier,
                navController,
                mainViewModel
            )
        }
        composable("LogScreen") {
            LogScreen()
        }
    }
}
