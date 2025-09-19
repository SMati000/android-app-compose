package com.example.loamtp1.ui.auxiliar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConversorUnidades() {
    val unidades = listOf("Metros", "Kilómetros", "Millas")

    var valorEntrada by remember { mutableStateOf("") }
    var unidadOrigen by remember { mutableStateOf(unidades.first()) }
    var unidadDestino by remember { mutableStateOf(unidades[1]) }
    var resultado by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxHeight(0.8f),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Conversor de Unidades",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de texto
        OutlinedTextField(
            value = valorEntrada,
            onValueChange = { valorEntrada = it },
            label = { Text("Valor") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Selector unidad origen
        UnidadDropdown(
            label = "De",
            opciones = unidades,
            seleccion = unidadOrigen,
            onSeleccionChange = { unidadOrigen = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Selector unidad destino
        UnidadDropdown(
            label = "A",
            opciones = unidades,
            seleccion = unidadDestino,
            onSeleccionChange = { unidadDestino = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                resultado = convertir(valorEntrada, unidadOrigen, unidadDestino)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Convertir")
        }

        Spacer(modifier = Modifier.height(24.dp))

        resultado?.let {
            Text(
                text = "Resultado: $it",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun UnidadDropdown(
    label: String,
    opciones: List<String>,
    seleccion: String,
    onSeleccionChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label, color = MaterialTheme.colorScheme.primary)
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(seleccion)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            onSeleccionChange(opcion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

fun convertir(valorStr: String, origen: String, destino: String): String {
    val valor = valorStr.toDoubleOrNull() ?: return "Número inválido"

    val enMetros = when (origen) {
        "Metros" -> valor
        "Kilómetros" -> valor * 1000
        "Millas" -> valor * 1609.34
        else -> valor
    }

    val convertido = when (destino) {
        "Metros" -> enMetros
        "Kilómetros" -> enMetros / 1000
        "Millas" -> enMetros / 1609.34
        else -> enMetros
    }

    return "%.2f $destino".format(convertido)
}
