package com.example.loamtp1.ui.auxiliar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loamtp1.models.ChatViewModel
import com.example.loamtp1.models.Mensaje

@Composable
fun ChatPantalla(viewModel: ChatViewModel = viewModel()) {
    val mensajes = viewModel.mensajes
    val input = viewModel.input

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        ) {
            items(mensajes) { msg ->
                BurbujaChat(msg)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = input,
                onValueChange = { viewModel.onInputChange(it) },
                placeholder = { Text("Escribir mensaje...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    viewModel.enviarMensaje()
                }
            ) {
                Text("Enviar")
            }
        }
    }
}

@Composable
fun BurbujaChat(mensaje: Mensaje) {
    val bgColor = if (mensaje.esUsuario) Color(0xFF3243A1) else Color(0xFF884D2D)
    val align = if (mensaje.esUsuario) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        horizontalArrangement = align
    ) {
        Box(
            modifier = Modifier
                .background(bgColor, shape = MaterialTheme.shapes.medium)
                .padding(10.dp)
        ) {
            Text(
                text = mensaje.texto,
                textAlign = if (mensaje.esUsuario) TextAlign.End else TextAlign.Start
            )
        }
    }
}
