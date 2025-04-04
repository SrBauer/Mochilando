package com.example.mochilando.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(viewModel: TravelViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Minhas Viagens", fontSize = 20.sp, color = Color(0xFF135937))

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.trips.isEmpty()) {
            Text("Nenhuma viagem cadastrada ainda.", color = Color.Gray)
        } else {
            viewModel.trips.forEach { trip ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Destino: ${trip.destination}", fontSize = 18.sp)
                        Text("Tipo: ${trip.travelType}", fontSize = 16.sp)
                        Text("Início: ${trip.startDate}", fontSize = 16.sp)
                        Text("Término: ${trip.endDate}", fontSize = 16.sp)
                        Text("Orçamento: ${trip.budget}", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
