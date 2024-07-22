package com.alexru.dufanyi.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alexru.dufanyi.ui.AppDestination
import com.alexru.dufanyi.ui.reader.ReaderUiState

@Composable
fun BottomNavigation(
    visibleState: Boolean,
    appScreens: List<AppDestination>,
    onTabSelected: (AppDestination) -> Unit,
    currentScreen: AppDestination,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visibleState,
        enter = EnterTransition.None,
        exit = ExitTransition.None,
        content = {
            BottomNavigationBar(
                appScreens = appScreens,
                onTabSelected = onTabSelected,
                currentScreen = currentScreen,
                modifier = modifier
            )
        }
    )
}

@Composable
fun BottomNavigationBar(
    appScreens: List<AppDestination>,
    onTabSelected: (AppDestination) -> Unit,
    currentScreen: AppDestination,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier
    ) {
        appScreens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = screen.route.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    )
                },
                selected = currentScreen.route == screen.route,
                onClick = { onTabSelected(screen) }
            )
        }
    }
}

@Composable
fun ReaderBottomBar(
    state: ReaderUiState,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = state.showBars,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = modifier
            ) {

            }
        }
    )

}