package com.example.loamtp1.ui.principal

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CameraFront
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.loamtp1.ui.activities.CamaraActivity
import com.example.loamtp1.ui.auxiliar.GrabadorAudio

@Composable
fun RowGrabar(modifier: Modifier = Modifier) {
    val context = LocalContext.current // Activity hosting the Composable
    var grabandoAudio by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header
            Text(
                text = "Grabar",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if(grabandoAudio) {
                    GrabadorAudio(
                        modifier = modifier,
                        contexto = context,
                        onDetener = { grabandoAudio = false })
                    return
                }

                ActionButton(
                    icon = Icons.Filled.CameraAlt,
                    text = "Camara",
                    contentDescription = "Abrir camara trasera",
                    onClick = {
                        val intent = Intent(context, CamaraActivity::class.java)
                        intent.putExtra("CAMERA_TYPE", "BACK")
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f)
                )

                ActionButton(
                    icon = Icons.Filled.CameraFront,
                    text = "Selfie",
                    contentDescription = "Abrir camara frontal",
                    onClick = {
                        val intent = Intent(context, CamaraActivity::class.java)
                        intent.putExtra("CAMERA_TYPE", "FRONT")
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f)
                )

                ActionButton(
                    icon = Icons.Filled.Mic,
                    text = "Audio",
                    contentDescription = "Grabar audio",
                    onClick = { grabandoAudio = true },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
