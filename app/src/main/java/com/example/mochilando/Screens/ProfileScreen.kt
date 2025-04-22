package com.example.mochilando.Screens

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import com.example.mochilando.DataBase.AppDatabase
import com.example.mochilando.Entity.Trip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current

    var destination by remember { mutableStateOf("") }
    var travelType by remember { mutableStateOf("Lazer") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var budgetText by remember { mutableStateOf("R$ 0,00") }
    var budgetValue by remember { mutableStateOf(0L) }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {}
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Nova Viagem", fontSize = 20.sp, color = Color(0xFF135937))

            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Destino") },
                modifier = Modifier.fillMaxWidth()
            )

            // Tipo de Viagem (Dropdown)
            DropdownMenuTripTypeProfile(selected = travelType, onTypeChange = { travelType = it })

            val context = LocalContext.current
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            DatePickerFieldProfile(
                label = "Data de Início",
                date = startDate,
                onDateSelected = { startDate = it }
            )

            DatePickerFieldProfile(
                label = "Data Final",
                date = endDate,
                minDate = try {
                    formatter.parse(startDate)?.time ?: System.currentTimeMillis()
                } catch (e: Exception) {
                    System.currentTimeMillis()
                },
                onDateSelected = { selectedDate ->
                    val start = formatter.parse(startDate)
                    val end = formatter.parse(selectedDate)
                    if (start != null && end != null && end.before(start)) {
                        Toast.makeText(context, "A data final não pode ser menor que a data inicial", Toast.LENGTH_SHORT).show()
                    } else {
                        endDate = selectedDate
                    }
                }
            )

            // Orçamento com máscara
            OutlinedTextField(
                value = budgetText,
                onValueChange = { input ->
                    val cleanInput = input.filter { it.isDigit() }
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

            val scope = rememberCoroutineScope()

            Button(
                onClick = {
                    if (destination.isNotBlank() && startDate.isNotBlank() && endDate.isNotBlank()) {
                        val trip = Trip(
                            destiny = destination,
                            type = travelType,
                            startDate = convertDateToIso(startDate),
                            endDate = convertDateToIso(endDate),
                            budget = budgetValue / 100.0
                        )

                        scope.launch {
                            AppDatabase.getDatabase(context).tripDao().insertTrip(trip)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Viagem salva com sucesso!", Toast.LENGTH_SHORT).show()
                                navController.navigate("listTrip")
                            }
                        }
                    } else {
                        Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF135937))
            ) {
                Text("Salvar", color = Color.White)
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuTripTypeProfile(selected: String, onTypeChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val tripTypes = listOf("Lazer", "Negócios")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selected,
            onValueChange = { },
            readOnly = true,
            label = { Text("Tipo de Viagem") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            tripTypes.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onTypeChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DatePickerFieldProfile(
    label: String,
    date: String,
    minDate: Long = System.currentTimeMillis(), // padrão: hoje
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            onDateSelected(dateFormatter.format(calendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Define a data mínima no DatePicker
    datePickerDialog.datePicker.minDate = minDate

    // Campo de texto clicável com ícone de calendário
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() } // click fora do campo
    ) {
        OutlinedTextField(
            value = date,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Selecionar data"
                )
            },
            enabled = false, // desabilitado para não abrir teclado
            modifier = Modifier.fillMaxWidth()
        )
    }
}


fun formatCurrency(value: Long): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return numberFormat.format(value / 100.0)
}
fun convertDateToIso(date: String): String {
    return try {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        outputFormat.format(inputFormat.parse(date)!!)
    } catch (e: Exception) {
        date
    }
}


