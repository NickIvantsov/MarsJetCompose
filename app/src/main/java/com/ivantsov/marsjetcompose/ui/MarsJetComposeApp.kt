package com.ivantsov.marsjetcompose.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ivantsov.marsjetcompose.ui.theme.MarsJetComposeTheme

@Composable
fun MarsJetComposeApp() {
    MarsJetComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {

        }
    }
}