package com.example.loamtp1.ui.principal

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.loamtp1.ui.activities.MapaActivity
import com.example.loamtp1.ui.activities.MedirActivity
import com.example.loamtp1.ui.activities.RAActivity

@Composable
fun RowEspacio(modifier: Modifier = Modifier) {
    val context = LocalContext.current

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
                text = "Espacio",
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
                    icon = Icons.Filled.LocationOn,
                    text = "Localización",
                    contentDescription = "Abrir mapa",
                    onClick = {
                        val intent = Intent(context, MapaActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f)
                )

                ActionButton(
                    icon = Icons.Filled.Cable,
                    text = "Medir\nDistancia",
                    contentDescription = "Medir distancia",
                    onClick = {
                        val intent = Intent(context, MedirActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f)
                )

                ActionButton(
                    icon = Icons.Filled.VideogameAsset,
                    text = "Realidad\nAumentada",
                    contentDescription = "Realidad aumentada",
                    onClick = {
                        val intent = Intent(context, RAActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
