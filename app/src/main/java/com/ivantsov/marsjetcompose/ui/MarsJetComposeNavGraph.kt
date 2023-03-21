package com.ivantsov.marsjetcompose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ivantsov.marsjetcompose.ui.home.HomeRoute
import com.ivantsov.marsjetcompose.ui.photos.PhotosRoute

@Composable
fun MarsJetComposeNavGraph(
    navigateToPhotos: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MarsJetComposeNavigation.HOME_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = MarsJetComposeNavigation.HOME_ROUTE) {
            HomeRoute(
                homeViewModel = hiltViewModel(),
                navigateToPhotos = navigateToPhotos
            )
        }
        composable(route = MarsJetComposeNavigation.PHOTO_LIST_ROUTE) {
            PhotosRoute(viewModel = hiltViewModel())
        }
    }
}