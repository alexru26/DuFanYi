package com.alexru.dufanyi

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alexru.dufanyi.database.dao.SeriesDao
import kotlinx.coroutines.launch
import com.alexru.dufanyi.networking.ShukuClient
import com.alexru.dufanyi.ui.browse.BrowseScreen
import com.alexru.dufanyi.ui.library.LibraryScreen
import com.alexru.dufanyi.ui.series.SeriesScreen
import com.alexru.dufanyi.ui.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    seriesEntities: List<com.alexru.dufanyi.database.entity.Series>,
    seriesDao: SeriesDao,
    shukuClient: ShukuClient,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
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
                series = seriesEntities,
                onSeriesClick = { seriesId -> navController.navigateToSeriesScreen(seriesId) }
            )
        }
        composable(route = Browse.route) {
            BrowseScreen(
                shukuClient = shukuClient,
                onUpload = { series ->
                    scope.launch {
                        seriesDao.upsert(series)
                    }
                }
            )
        }
        composable(route = Settings.route) {
            SettingsScreen(

            )
        }
        composable(
            route = Series.routeWithArgs,
            arguments = Series.arguments,
            enterTransition = { slideInHorizontally(
                animationSpec = tween(durationMillis = 200),
                initialOffsetX = { fullWidth -> fullWidth/3 }
            ) + fadeIn(
                animationSpec = tween(durationMillis = 500)
            )},
            exitTransition = { fadeOut(
                animationSpec = tween(durationMillis = 1)
            )},
            popEnterTransition = { slideInHorizontally(
                animationSpec = tween(durationMillis = 200),
                initialOffsetX = { fullWidth -> -fullWidth/3 }

            ) + fadeIn(
                animationSpec = tween(durationMillis = 500)
            )},
            popExitTransition = { slideOutHorizontally(
                animationSpec = tween(durationMillis = 400),
                targetOffsetX = { fullWidth -> fullWidth }
            ) + fadeOut(
                animationSpec = tween(durationMillis = 150)
            )},
        ) { navBackStackEntry ->
            val seriesId = navBackStackEntry.arguments?.getLong(Series.seriesIdArgument)

            SeriesScreen(
                seriesEntities = seriesEntities,
                seriesId = seriesId
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

private fun NavHostController.navigateToSeriesScreen(seriesId: Long) {
    this.navigateSingleTopTo("${Series.route}/$seriesId")
}