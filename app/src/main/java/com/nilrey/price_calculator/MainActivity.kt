package com.nilrey.price_calculator

import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import com.nilrey.price_calculator.ui.theme.Price_calculatorTheme
import com.nilrey.price_calculator.R
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale("ru")
        Locale.setDefault(locale)
        val config = Configuration(newBase?.resources?.configuration)
        config.setLocale(locale)
        val context = newBase?.createConfigurationContext(config)
        super.attachBaseContext(context)
    }
    
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
    var lastPrice by remember { mutableStateOf<Double?>(null) }
    var lastVolume by remember { mutableStateOf<Double?>(null) }

    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(R.string.info_text),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        item {
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text(stringResource(R.string.label_price)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            OutlinedTextField(
                value = volume,
                onValueChange = { volume = it },
                label = { Text(stringResource(R.string.label_volume)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Button(
                onClick = {
                    val priceVal = price.toDoubleOrNull()
                    val volumeVal = volume.toDoubleOrNull()
                    if (priceVal != null && volumeVal != null && volumeVal != 0.0) {
                        result = (priceVal * 1000) / volumeVal
                        lastPrice = priceVal
                        lastVolume = volumeVal
                        showCompare = true
                        price = ""
                        volume = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.button_calculate))
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (result != null) {
            item {
                Text(
                    "%.1f".format(result),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 48.sp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
        if (showCompare && result != null) {
            item {
                Button(
                    onClick = {
                        compareList = compareList + CompareEntry(lastPrice!!, lastVolume!!, result!!)
                        showCompare = false
                        result = null
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.button_add_to_compare))
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        if (compareList.isNotEmpty()) {
            item {
                Text(stringResource(R.string.label_comparison_table), style = MaterialTheme.typography.titleMedium)
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            // Заголовки таблицы
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.column_price), modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
                    Text(stringResource(R.string.column_volume), modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
                    Text(stringResource(R.string.column_compare), modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
                }
            }
            item {
                Divider()
            }
            items(count = compareList.size) { index ->
                val entry = compareList[index]
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

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    Price_calculatorTheme {
        CalculatorScreen()
    }
}