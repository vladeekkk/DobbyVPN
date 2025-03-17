package com.dobby.navigation

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dobby.feature.logging.presentation.LogsViewModel
import com.dobby.feature.logging.ui.LogScreen
import com.dobby.feature.main.presentation.MainViewModel
import com.dobby.feature.main.ui.AwgScreen
import com.dobby.feature.main.ui.DobbySocksScreen

@Composable
fun App(
    mainViewModel: MainViewModel,
    logsViewModel: LogsViewModel,
) {
    val navController = rememberNavController()
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = { keyboardController?.hide() })
            },
        bottomBar = {
            var selectedItem by remember { mutableIntStateOf(0) }
            val items = listOf("Outline", "AmneziaWG", "Logs")
            val screens = listOf(MainScreen, AmneziaWGScreen, LogsScreen)
            val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Favorite, Icons.Filled.Star)

            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                selectedIcons[index],
                                contentDescription = item
                            )
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(screens[index])
                        }
                    )
                }
            }
        },
        content = { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = MainScreen
            ) {
                composable<MainScreen> {
                    DobbySocksScreen(mainViewModel)
                }
                composable<AmneziaWGScreen> {
                    AwgScreen(mainViewModel)
                }
                composable<LogsScreen> {
                    LogScreen(logsViewModel)
                }
            }
        }
    )
}
