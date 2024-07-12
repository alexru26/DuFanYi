package com.alexru.dufanyi

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface AppDestination {
    val icon: ImageVector
    val route: String
    val routeWithArgs: String
}

object Library: AppDestination {
    override val icon = Icons.Default.Home
    override val route = "library"
    override val routeWithArgs = route
}

object Browse: AppDestination {
    override val icon = Icons.Default.TravelExplore
    override val route = "browse"
    override val routeWithArgs = route
}

object Settings: AppDestination {
    override val icon = Icons.Default.Settings
    override val route = "settings"
    override val routeWithArgs = route
}

object Series: AppDestination {
    override val icon = Icons.AutoMirrored.Filled.MenuBook
    override val route = "series"
    const val seriesIdArgument = "id"
    override val routeWithArgs = "$route/{$seriesIdArgument}"
    val arguments = listOf(
        navArgument(seriesIdArgument) { type = NavType.LongType },
    )
}

object Reader: AppDestination {
    override val icon = Icons.Default.Book
    override val route = "reader"
    const val readerIdArgument = "id"
    override val routeWithArgs = "${route}/{$readerIdArgument}"
    val arguments = listOf(
        navArgument(readerIdArgument) { type = NavType.LongType }
    )
}

val navBarAppScreens = listOf(Library, Browse, Settings)

val appScreens = listOf(Library, Browse, Settings, Series, Reader)