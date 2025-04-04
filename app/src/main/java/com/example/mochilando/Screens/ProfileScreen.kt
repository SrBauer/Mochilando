package com.example.mochilando.Screens

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ProfileScreen(navController: NavController, viewModel: TravelViewModel) {

    val context = LocalContext.current

    var destination by remember { mutableStateOf("") }
    var travelType by remember { mutableStateOf("Lazer") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    var budgetValue by remember { mutableStateOf(0L) }
    var budgetText by remember { mutableStateOf("R$ 0,00") }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {  },

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Nova Viagem", fontSize = 20.sp, color = Color(0xFF135937))

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Destino") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(26.dp))

            Text(text = "Tipo de Viagem", fontSize = 20.sp, color = Color(0xFF135937))

            Spacer(modifier = Modifier.height(26.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RadioButton(selected = travelType == "Lazer", onClick = { travelType = "Lazer" })
                Text("Lazer", fontSize = 20.sp, color = Color(0xFF135937))
                RadioButton(selected = travelType == "Negócios", onClick = { travelType = "Negócios" })
                Text("Negócios", fontSize = 20.sp, color = Color(0xFF135937))
            }

            Spacer(modifier = Modifier.height(26.dp))

            OutlinedTextField(
                value = startDate,
                onValueChange = { },
                label = { Text("Data de Início") },
                readOnly = true,
                trailingIcon = {

                    IconButton(onClick = {
                        selectDate(context) { selectedDate ->
                            startDate = selectedDate
                        }
                    }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Selecionar data")
                    }

                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(26.dp))

            OutlinedTextField(
                value = endDate,
                onValueChange = { },
                label = { Text("Data Final") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        // Verifica se a data de início foi definida
                        if (startDate.isNotEmpty()) {
                            val parts = startDate.split("/")
                            if (parts.size == 3) {
                                val calendarStart = Calendar.getInstance()
                                calendarStart.set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())

                                selectDate(context, calendarStart.timeInMillis) { selectedDate ->
                                    endDate = selectedDate
                                }
                            }
                        } else {

                        }
                    }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Selecionar data")
                    }

                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Campo de orçamento formatado com "R$"
            OutlinedTextField(
                value = budgetText,
                onValueChange = { input ->
                    val cleanInput = input.filter { it.isDigit() } // Apenas números
                    val newValue = cleanInput.toLongOrNull() ?: 0L
                    budgetValue = newValue
                    budgetText = formatCurrency(newValue)
                },
                label = { Text("Orçamento") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val newTrip = Trip(destination, travelType, startDate, endDate, budgetText)
                    viewModel.addTrip(newTrip)
                    navController.navigate("HomeScreen") // Volta para HomeScreen
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF135937))
            ) {
                Text("Salvar", color = Color.White)
            }
        }
    }
}

// Função para exibir o DatePickerDialog
fun selectDate(context: Context, minDate: Long? = null, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        onDateSelected("$selectedDay/${selectedMonth + 1}/$selectedYear")
    }, year, month, day)

    // Define a data mínima, se fornecida
    if (minDate != null) {
        datePickerDialog.datePicker.minDate = minDate
    } else {
        datePickerDialog.datePicker.minDate = calendar.timeInMillis // data de hoje por padrão
    }

    datePickerDialog.show()
}


// Função para salvar as informações da viagem
fun saveTripInfo(destination: String, tripType: String, startDate: String, endDate: String, budget: String) {
    Log.d("TripInfo", "Destino: $destination")
    Log.d("TripInfo", "Tipo: $tripType")
    Log.d("TripInfo", "Início: $startDate")
    Log.d("TripInfo", "Término: $endDate")
    Log.d("TripInfo", "Orçamento: $budget")
}

// Função para formatar o orçamento corretamente
fun formatCurrency(value: Long): String {
    val formatted = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value / 100.0)
    return formatted.replace("R$ ", "R$")
}
