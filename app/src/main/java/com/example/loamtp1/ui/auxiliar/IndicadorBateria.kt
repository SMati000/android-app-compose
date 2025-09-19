package com.example.loamtp1.ui.auxiliar

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChargingStation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun IndicadorBateria(
    porcentajeBateria: Int,
    tiempoRestante: String,
    cargando: Boolean = false,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        if (cargando) {
            Icon(
                imageVector = Icons.Filled.ChargingStation,
                contentDescription = "Cargando",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
        }

        BarraBateria(
            porcentaje = porcentajeBateria,
            cargando = cargando,
            modifier = modifier.weight(5f)
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = tiempoRestante,
            style = MaterialTheme.typography.bodySmall,
            color = getColorTexto(porcentajeBateria),
            modifier = modifier.weight(1f)
        )
    }
}

@Composable
private fun BarraBateria(
    porcentaje: Int,
    cargando: Boolean,
    modifier: Modifier = Modifier
) {
    val porcentajeAnimado by animateFloatAsState(
        targetValue = porcentaje / 100f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "porcentaje_bateria"
    )

    Box(
        modifier = modifier
            .height(16.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(porcentajeAnimado)
                .background(
                    color = getColorBateria(porcentaje, cargando),
                    shape = RoundedCornerShape(8.dp)
                )
        )

        Text(
            text = "$porcentaje%",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.Center) // Centers the text inside the battery
                .padding(horizontal = 4.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 1.dp)
                .width(2.dp)
                .height(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(
                        topEnd = 1.dp,
                        bottomEnd = 1.dp
                    )
                )
        )
    }
}

@Composable
private fun getColorBateria(percentage: Int, isCharging: Boolean): Color {
    return when {
        isCharging -> MaterialTheme.colorScheme.primary
        percentage <= 15 -> Color(0xFFE53E3E)
        percentage <= 30 -> Color(0xFFFF8C00)
        percentage <= 50 -> Color(0xFFFFC107)
        else -> Color(0xFF4CAF50)
    }
}

@Composable
private fun getColorTexto(percentage: Int): Color {
    return when {
        percentage <= 15 -> Color(0xFFE53E3E) // Red
        percentage <= 30 -> Color(0xFFFF8C00) // Orange
        else -> MaterialTheme.colorScheme.onSurface
    }
}
