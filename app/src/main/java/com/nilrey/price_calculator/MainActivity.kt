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
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.text.input.TextFieldValue
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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    var price by remember { mutableStateOf("") }
    var volume by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Double?>(null) }
    var showCompare by remember { mutableStateOf(false) }
    var compareList by remember { mutableStateOf(listOf<Double>()) }

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
                    compareList = compareList + result!!
                    showCompare = false
                    result = null
                    price = ""
                    volume = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Compare")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (compareList.isNotEmpty()) {
            Text("Comparison Table:", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(compareList) { item ->
                    Text("${"%.2f".format(item)}", modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Price_calculatorTheme {
        Greeting("Android")
    }
}