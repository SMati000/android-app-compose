package com.example.loamtp1.ui.principal

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkUnreadChatAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loamtp1.models.BateriaViewModel
import com.example.loamtp1.ui.activities.ChatActivity
import com.example.loamtp1.ui.auxiliar.IndicadorBateria

@Composable
fun AppFooter(modifier: Modifier = Modifier, viewModel: BateriaViewModel = viewModel()) {
    Row(
        modifier = Modifier
            .background(Color(0xFF03DAC5))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        val context = LocalContext.current

        val batteryInfo by viewModel.infoBateria.collectAsState()

        IndicadorBateria(
            porcentajeBateria = batteryInfo.porcentaje,
            tiempoRestante = batteryInfo.tiempoRestante,
            cargando = batteryInfo.cargando,
            modifier = modifier.weight(7f)
        )

        IconButton(
            onClick = {
                val intent = Intent(context, ChatActivity::class.java)
                context.startActivity(intent)
            },
        ) {
            Icon(
                imageVector = Icons.Filled.MarkUnreadChatAlt,
                contentDescription = "Chat",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
