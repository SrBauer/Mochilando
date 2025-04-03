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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.NumberFormat
import java.util.*

@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current

    var destination by remember { mutableStateOf("") }
    var tripType by remember { mutableStateOf("Lazer") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Nova Viagem", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("Destino") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(26.dp))

        Text(text = "Motivo da Viagem")
        Row(modifier = Modifier.fillMaxWidth()) {
            RadioButton(selected = tripType == "Lazer", onClick = { tripType = "Lazer" })
            Text("Lazer", modifier = Modifier.padding(start = 8.dp))
            Spacer(modifier = Modifier.width(26.dp))
            RadioButton(selected = tripType == "Negócio", onClick = { tripType = "Negócio" })
            Text("Negócio", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(26.dp))

        OutlinedTextField(
            value = startDate,
            onValueChange = { },
            label = { Text("Data de Início") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { selectDate(context) { startDate = it } }) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = "Selecionar data")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = endDate,
            onValueChange = { },
            label = { Text("Data Final") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { selectDate(context) { endDate = it } }) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = "Selecionar data")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de orçamento formatado com "R$"
        OutlinedTextField(
            value = budget,
            onValueChange = { input ->
                val cleanInput = input.filter { it.isDigit() } // Permite apenas números
                budget = formatCurrency(cleanInput)
            },
            label = { Text("Orçamento") },
            placeholder = { Text("Digite o orçamento estimado") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botão para salvar os dados
        Button(
            onClick = {
                saveTripInfo(destination, tripType, startDate, endDate, budget)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar")
        }
    }
}

// Função para exibir o DatePickerDialog
fun selectDate(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        onDateSelected("$selectedDay/${selectedMonth + 1}/$selectedYear")
    }, year, month, day).show()
}

fun saveTripInfo(destination: String, tripType: String, startDate: String, endDate: String, budget: String) {
    Log.d("TripInfo", "Destino: $destination")
    Log.d("TripInfo", "Tipo: $tripType")
    Log.d("TripInfo", "Início: $startDate")
    Log.d("TripInfo", "Término: $endDate")
    Log.d("TripInfo", "Orçamento: $budget")
}

// Função para formatar o orçamento como moeda
fun formatCurrency(input: String): String {
    return if (input.isNotEmpty()) {
        val number = input.toLongOrNull() ?: 0L
        val formatted = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(number / 100.0)
        formatted.replace("R$ ", "R$") // Ajuste para remover espaço extra
    } else {
        ""
    }
}
