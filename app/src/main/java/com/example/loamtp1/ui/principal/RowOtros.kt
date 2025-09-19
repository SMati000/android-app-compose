package com.example.loamtp1.ui.principal

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
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
import com.example.loamtp1.ui.activities.UnidadesActivity

@Composable
fun RowOtros(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraId: String = cameraManager.cameraIdList.firstOrNull() as String
    var linternaEncendida by remember { mutableStateOf(false) }

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
                text = "Otros",
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
                ActionButton(
                    icon = if (linternaEncendida) Icons.Filled.FlashlightOn else Icons.Filled.FlashlightOff,
                    text = "Linterna",
                    contentDescription = "Abrir linterna",
                    onClick = {
                        linternaEncendida = !linternaEncendida
                        try {
                            cameraManager.setTorchMode(cameraId, linternaEncendida)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    modifier = Modifier.weight(1f)
                )

                ActionButton(
                    icon = Icons.Filled.Architecture,
                    text = "Unidades",
                    contentDescription = "Convertir unidades",
                    onClick = {
                        val intent = Intent(context, UnidadesActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
