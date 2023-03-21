package com.ivantsov.marsjetcompose.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ivantsov.marsjetcompose.ui.theme.MarsJetComposeTheme

@Composable
fun MarsJetComposeApp() {
    MarsJetComposeTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            MarsJetComposeNavigationActions(navController)
        }

        val coroutineScope = rememberCoroutineScope()

        val navBackStackEntry by navController.currentBackStackEntryAsState()

        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
        ) {
            MarsJetComposeNavGraph(
                navController = navController,
                navigateToPhotos = navigationActions.navigateToPhotoList
            )
        }
    }
}