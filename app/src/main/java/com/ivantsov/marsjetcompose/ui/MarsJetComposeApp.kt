package com.ivantsov.marsjetcompose.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ivantsov.marsjetcompose.ui.home.HomeViewModel
import com.ivantsov.marsjetcompose.ui.home.MainHomeScreen
import com.ivantsov.marsjetcompose.ui.theme.MarsJetComposeTheme

@Composable
fun MarsJetComposeApp(homeViewModel: HomeViewModel) {
    MarsJetComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            MainHomeScreen(homeViewModel)
        }
    }
}