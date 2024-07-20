package com.alexru.dufanyi.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alexru.dufanyi.ui.browse.BrowseScreen
import com.alexru.dufanyi.ui.components.enterTransition
import com.alexru.dufanyi.ui.components.exitTransition
import com.alexru.dufanyi.ui.components.popEnterTransition
import com.alexru.dufanyi.ui.components.popExitTransition
import com.alexru.dufanyi.ui.library.LibraryScreen
import com.alexru.dufanyi.ui.reader.ReaderScreen
import com.alexru.dufanyi.ui.series.SeriesScreen
import com.alexru.dufanyi.ui.series.SeriesViewModel
import com.alexru.dufanyi.ui.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Library.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        modifier = modifier
    ) {
        composable(route = Library.route) {
            LibraryScreen(
                onSeriesClick = { seriesId -> navController.navigateToSeriesScreen(seriesId) }
            )
        }
        composable(route = Browse.route) {
            BrowseScreen()
        }
        composable(route = Settings.route) {
            SettingsScreen()
        }
        composable(
            route = Series.routeWithArgs,
            arguments = Series.arguments,
        ) { navBackStackEntry ->
            val seriesId = navBackStackEntry.arguments?.getLong(Series.seriesIdArgument)
            SeriesScreen(
                onChapterClick = { chapterId ->
                    if(seriesId != null) {
                        navController.navigateToReaderScreen(seriesId, chapterId)
                    }
                 },
                onNavigateBack = { navController.navigateUp() },
            )
        }
        composable(
            route = Reader.routeWithArgs,
            arguments = Reader.arguments,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() },
        ) { navBackStackEntry ->
            val seriesId = navBackStackEntry.arguments?.getLong(Reader.seriesIdArgument)
            val chapterId = navBackStackEntry.arguments?.getLong(Reader.chapterIdArgument)

            ReaderScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
//        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
//            saveState = true
//        }
        launchSingleTop = true
        restoreState = true
    }

private fun NavHostController.navigateToSeriesScreen(seriesId: Long) {
    this.navigateSingleTopTo("${Series.route}/$seriesId")
}

private fun NavHostController.navigateToReaderScreen(seriesId: Long, chapterId: Long) {
    this.navigateSingleTopTo("${Reader.route}/$seriesId/$chapterId")
}