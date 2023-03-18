package com.ivantsov.marsjetcompose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ivantsov.marsjetcompose.data.reprository.PhotosRepository
import com.ivantsov.marsjetcompose.di.IoDispatcher
import com.ivantsov.marsjetcompose.ui.home.HomeRoute
import com.ivantsov.marsjetcompose.ui.home.HomeViewModel
import com.ivantsov.marsjetcompose.ui.photos.PhotosRoute
import com.ivantsov.marsjetcompose.ui.photos.PhotosViewModel
import kotlinx.coroutines.CoroutineDispatcher

@Composable
fun MarsJetComposeNavGraph(
    photosRepository: PhotosRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
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
        composable(MarsJetComposeNavigation.HOME_ROUTE) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory()
            )
            HomeRoute(
                homeViewModel = homeViewModel,
                navigateToPhotos = navigateToPhotos
            )
        }
        composable(MarsJetComposeNavigation.PHOTO_LIST_ROUTE) {
            val photosViewModel: PhotosViewModel = viewModel(
                factory = PhotosViewModel.provideFactory(photosRepository, ioDispatcher)
            )
            PhotosRoute(viewModel = photosViewModel)
        }
    }
}