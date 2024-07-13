package com.alexru.dufanyi

import android.view.WindowInsetsController
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alexru.dufanyi.database.dao.SeriesDao
import kotlinx.coroutines.launch
import com.alexru.dufanyi.ui.browse.BrowseScreen
import com.alexru.dufanyi.ui.components.enterTransition
import com.alexru.dufanyi.ui.components.exitTransition
import com.alexru.dufanyi.ui.components.popEnterTransition
import com.alexru.dufanyi.ui.components.popExitTransition
import com.alexru.dufanyi.ui.library.LibraryScreen
import com.alexru.dufanyi.ui.reader.ReaderScreen
import com.alexru.dufanyi.ui.series.SeriesScreen
import com.alexru.dufanyi.ui.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    seriesDao: SeriesDao,
    modifier: Modifier = Modifier
) {
    val seriesList by seriesDao.getAllSeriesWithChapters().collectAsState(initial = emptyList())
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
                seriesList = seriesList,
                onSeriesClick = { seriesId -> navController.navigateToSeriesScreen(seriesId) }
            )
        }
        composable(route = Browse.route) {
            BrowseScreen(
                seriesDao = seriesDao,
            )
        }
        composable(route = Settings.route) {
            SettingsScreen(

            )
        }
        composable(
            route = Series.routeWithArgs,
            arguments = Series.arguments,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() },
        ) { navBackStackEntry ->
            val seriesId = navBackStackEntry.arguments?.getLong(Series.seriesIdArgument)

            SeriesScreen(
                seriesList = seriesList,
                seriesId = seriesId,
                onChapterClick = { chapterId ->
                    if(seriesId != null) {
                        navController.navigateToReaderScreen(seriesId, chapterId)
                    }
                 },
                onNavigateBack = { navController.navigateUp() },
                onDeleteSeries = {
                    val series = seriesList.find { it.series.seriesId == seriesId }
                    scope.launch {
                        if (series != null) {
                            seriesDao.deleteSeries(series.series)
                            series.chapters.forEach { seriesDao.deleteChapter(it) }
                        }
                        navController.navigateSingleTopTo(Library.route)
                    }
                }
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
                seriesList = seriesList,
                seriesId = seriesId,
                chapterId = chapterId,
                onNavigateBack = {
                    navController.popBackStack()
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