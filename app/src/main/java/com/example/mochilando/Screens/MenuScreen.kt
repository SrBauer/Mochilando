package com.example.mochilando.Screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MenuScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen() {
    val navController = rememberNavController()

    Scaffold(
        containerColor = Color(0xFFF5F5F5), // Cor de fundo suave
        topBar = {
            TopAppBar(
                title = { Text(text = "Mochilando", color = Color.White) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF135937))
            )
        },
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            NavHost(navController = navController, startDestination = "HomeScreen") {
                composable("HomeScreen") { HomeScreen() }
                composable("Profile") { ProfileScreen(navController) }
                composable("About") { AboutScreen() }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any { it.route == "HomeScreen" } == true,
            onClick = { navController.navigate("HomeScreen") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Início", tint = Color(0xFF135937)) },
            label = { Text("Início", color = Color(0xFF135937)) }
        )
        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any { it.route == "Profile" } == true,
            onClick = { navController.navigate("Profile") },
            icon = { Icon(Icons.Default.AirplanemodeActive, contentDescription = "Nova Viagem", tint = Color(0xFF135937)) },
            label = { Text("Nova Viagem", color = Color(0xFF135937)) }
        )
        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any { it.route == "About" } == true,
            onClick = { navController.navigate("About") },
            icon = { Icon(Icons.Default.Info, contentDescription = "Sobre", tint = Color(0xFF135937)) },
            label = { Text("Sobre", color = Color(0xFF135937)) }
        )
    }
}

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Tela Principal", fontSize = 18.sp, color = Color(0xFF135937))
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Tela de Viagens", fontSize = 18.sp, color = Color(0xFF135937))

    }
}

@Composable
fun AboutScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Sobre o Mochilando", fontSize = 18.sp, color = Color(0xFF135937))
    }
}

@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    MenuScreen()
}