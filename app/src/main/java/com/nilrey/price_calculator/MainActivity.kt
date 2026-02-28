package com.nilrey.price_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nilrey.price_calculator.ui.theme.Price_calculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Price_calculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

data class CompareEntry(val price: Double, val volume: Double, val result: Double)

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    var price by remember { mutableStateOf("") }
    var volume by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Double?>(null) }
    var showCompare by remember { mutableStateOf(false) }
    var compareList by remember { mutableStateOf(listOf<CompareEntry>()) }

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = volume,
            onValueChange = { volume = it },
            label = { Text("Volume") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                val priceVal = price.toDoubleOrNull()
                val volumeVal = volume.toDoubleOrNull()
                if (priceVal != null && volumeVal != null && volumeVal != 0.0) {
                    result = (priceVal * 1000) / volumeVal
                    showCompare = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (result != null) {
            Text("Result: ${"%.2f".format(result)}", style = MaterialTheme.typography.titleMedium)
        }
        if (showCompare && result != null) {
            Button(
                onClick = {
                    val priceVal = price.toDoubleOrNull()!!
                    val volumeVal = volume.toDoubleOrNull()!!
                    compareList = compareList + CompareEntry(priceVal, volumeVal, result!!)
                    showCompare = false
                    result = null
                    price = ""
                    volume = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to Compare")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (compareList.isNotEmpty()) {
            Text("Comparison Table:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            // Заголовки таблицы
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Price", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
                Text("Volume", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
                Text("Compare", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
            }
            Divider()
            LazyColumn {
                items(compareList) { entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("%.2f".format(entry.price), modifier = Modifier.weight(1f))
                        Text("%.2f".format(entry.volume), modifier = Modifier.weight(1f))
                        Text("%.2f".format(entry.result), modifier = Modifier.weight(1f))
                    }
                    Divider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    Price_calculatorTheme {
        CalculatorScreen()
    }
}