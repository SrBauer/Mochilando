package com.example.mochilando.Screens

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.*

@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current

    var destination by remember { mutableStateOf("") }
    var tripType by remember { mutableStateOf("Lazer") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }


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
            Text("Lazer", modifier = Modifier.padding(start = 18.dp))
            Spacer(modifier = Modifier.width(26.dp))
            RadioButton(selected = tripType == "Negócio", onClick = { tripType = "Negócio" })
            Text("Negócio", modifier = Modifier.padding(start = 18.dp))
        }

        Spacer(modifier = Modifier.height(26.dp))

        Button(onClick = { selectDate(context) { startDate = it } }) {
            Text(if (startDate.isEmpty()) "Selecionar Data de Início" else "Início: $startDate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { selectDate(context) { endDate = it } }) {
            Text(if (endDate.isEmpty()) "Selecionar Data Final" else "Término: $endDate")
        }
    }
}

fun selectDate(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        onDateSelected("$selectedDay/${selectedMonth + 1}/$selectedYear")
    }, year, month, day).show()
}
