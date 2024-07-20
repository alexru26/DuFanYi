package com.alexru.dufanyi.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alexru.dufanyi.data.dao.SeriesDao
import com.alexru.dufanyi.ui.components.BottomNavigation

@Composable
fun DuFanYiApp() {
    val navController = rememberNavController()

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    val currentScreen = appScreens.find { it.routeWithArgs == currentDestination?.route } ?: Library

    var bottomBarState by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(true) {

    }

    bottomBarState = when(currentScreen) {
        Series, Reader -> {
            false
        }

        else -> {
            true
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigation(
                visibleState = bottomBarState,
                appScreens = navBarAppScreens,
                onTabSelected = { screen -> navController.navigateSingleTopTo(screen.route) },
                currentScreen = currentScreen
            )
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}