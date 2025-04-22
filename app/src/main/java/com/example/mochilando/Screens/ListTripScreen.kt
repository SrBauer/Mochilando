package com.example.mochilando.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mochilando.DataBase.AppDatabase
import com.example.mochilando.Entity.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTripsScreen() {
    val context = LocalContext.current
    val tripDao = AppDatabase.getDatabase(context).tripDao()
    val coroutineScope = rememberCoroutineScope()

    var trips by remember { mutableStateOf(emptyList<Trip>()) }
    var tripToDelete by remember { mutableStateOf<Trip?>(null) }

    // Carregar viagens ao iniciar
    LaunchedEffect(Unit) {
        trips = tripDao.getAllTrips().sortedByDescending { it.id }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Minhas Viagens") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(trips) { trip ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Destino: ${trip.destiny}", style = MaterialTheme.typography.titleMedium)
                        Text("Tipo: ${trip.type}", style = MaterialTheme.typography.bodyMedium)
                        Text("Início: ${formatDate(trip.startDate)}", style = MaterialTheme.typography.bodyMedium)
                        Text("Término: ${formatDate(trip.endDate)}", style = MaterialTheme.typography.bodyMedium)
                        Text("Orçamento: R$ ${"%.2f".format(trip.budget)}", style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                onClick = { tripToDelete = trip },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remover",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Remover")
                            }
                        }
                    }
                }
            }
        }

        tripToDelete?.let { trip ->
            AlertDialog(
                onDismissRequest = { tripToDelete = null },
                title = { Text("Confirmar exclusão") },
                text = { Text("Deseja realmente remover esta viagem?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                tripDao.deleteTrip(trip)
                                val updatedTrips = tripDao.getAllTrips().sortedByDescending { it.id }
                                trips = updatedTrips
                                tripToDelete = null
                            }
                        }
                    ) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { tripToDelete = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

fun formatDate(dateString: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = parser.parse(dateString)
        date?.let { formatter.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}
