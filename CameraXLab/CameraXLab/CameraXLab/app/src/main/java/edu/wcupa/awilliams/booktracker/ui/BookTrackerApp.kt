package edu.wcupa.awilliams.booktracker.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.wcupa.awilliams.booktracker.ui.screens.BookDetailsScreen
import edu.wcupa.awilliams.booktracker.ui.screens.BookEditScreen
import edu.wcupa.awilliams.booktracker.ui.screens.BookEntryScreen
import edu.wcupa.awilliams.booktracker.ui.screens.HomeScreen

@Composable
fun BookTrackerApp() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "home") {
        composable("home") { HomeScreen(nav) }
        composable("entry") { BookEntryScreen(nav) }
        composable("details/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toIntOrNull() ?: 0
            BookDetailsScreen(nav, id)
        }
        composable("edit/{id}") { backStack ->
            val id = backStack.arguments?.getString("id")?.toIntOrNull() ?: 0
            BookEditScreen(nav, id)
        }
    }
}
