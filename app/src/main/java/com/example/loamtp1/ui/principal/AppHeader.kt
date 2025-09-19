package com.example.loamtp1.ui.principal

import android.Manifest
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.loamtp1.BuildConfig
import com.example.loamtp1.R
import androidx.core.net.toUri
import com.example.loamtp1.ui.auxiliar.permisosParaComposable

@Composable
fun AppHeader(modifier: Modifier = Modifier) {
    val contexto = LocalContext.current
    val phoneNumber = BuildConfig.supportPhone

    val permisosHandler = permisosParaComposable(
        permisos = arrayOf(Manifest.permission.CALL_PHONE),
        onConcedidos = {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = "tel:$phoneNumber".toUri()
            }
            contexto.startActivity(intent)
        }
    )

    Row(modifier = modifier
        .fillMaxWidth()
        .padding(start = 16.dp, top = 20.dp, bottom = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.tool_box_64px),
            contentDescription = "Icono",
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "IngMobil",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { permisosHandler.handle() },
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Filled.Call,
                contentDescription = "Llamar",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
