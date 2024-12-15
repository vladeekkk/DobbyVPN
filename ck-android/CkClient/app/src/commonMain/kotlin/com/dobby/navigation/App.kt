package com.dobby.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dobby.logging.ui.LogScreen
import com.dobby.main.ui.DobbySocksScreen

@Composable
fun App(
    modifier: Modifier = Modifier,
    isConnected: State<Boolean> = object : State<Boolean> { override val value = false },
    initialConfig: String = "",
    initialKey: String = "",
    onConnectionButtonClick: (String?, String, Boolean) -> Unit = { _, _, _ -> }
) {
    val navController = androidx.navigation.compose.rememberNavController()

    NavHost(navController = navController, startDestination = "DobbySocksScreen") {
        composable("DobbySocksScreen") {
            DobbySocksScreen(
                modifier,
                isConnected,
                initialConfig,
                initialKey,
                onConnectionButtonClick,
                navController
            )
        }
        composable("LogScreen") {
            LogScreen()
        }
    }
}
