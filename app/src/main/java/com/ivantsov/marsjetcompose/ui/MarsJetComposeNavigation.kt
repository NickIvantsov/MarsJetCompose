package com.ivantsov.marsjetcompose.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object MarsJetComposeNavigation {
    const val HOME_ROUTE = "home"
    const val PHOTO_LIST_ROUTE = "photo_list"
}

class MarsJetComposeNavigationActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(MarsJetComposeNavigation.HOME_ROUTE) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    val navigateToPhotoList: () -> Unit = {
        navController.navigate(MarsJetComposeNavigation.PHOTO_LIST_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}