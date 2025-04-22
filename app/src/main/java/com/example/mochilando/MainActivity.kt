package com.example.mochilando

import LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mochilando.Screens.ListTripsScreen
import com.example.mochilando.Screens.MenuScreen
import com.example.mochilando.Screens.ProfileScreen
import com.example.mochilando.Screens.RegisterUserMainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable("login") { LoginScreen(navController) }
                        composable("register") { RegisterUserMainScreen(navController) }
                        composable("menu") { MenuScreen() }
                        composable("profile") { ProfileScreen(navController) } // <- Caso use
                    }
                }
            }
        }
    }
}
