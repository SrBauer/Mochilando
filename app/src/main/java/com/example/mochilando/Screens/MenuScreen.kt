package com.example.mochilando.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuScreen() {
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bem-vindo ao Mochilando!",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(onClick = { /* Adicionar navegação para outras telas */ }) {
                    Text("Explorar Destinos")
                }
                Button(onClick = { /* Adicionar navegação para perfil */ }) {
                    Text("Meu Perfil")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewMenuScreen() {
    MenuScreen()
}
