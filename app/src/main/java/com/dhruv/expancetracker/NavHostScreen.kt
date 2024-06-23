package com.dhruv.expancetracker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavHostScreen(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "/home")  {
        composable("/home") {
            HomeScreen(modifier ,navController)
        }
        composable("/addExpense") {
            AddExpance(modifier, navController)
        }
    }

}