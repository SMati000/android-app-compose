package com.example.loamtp1.ui.activities

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.loamtp1.ui.auxiliar.MedicionSimple
import com.example.loamtp1.ui.auxiliar.permisosParaActivity

class MedirActivity : ComponentActivity() {
    private val permisos = arrayOf(Manifest.permission.CAMERA)

    private val permisosHandler = permisosParaActivity(
        activity = this,
        permisos = permisos,
        onConcedidos = {
            setContent {
                MaterialTheme {
                    MedicionSimple()
                }
            }
        },
        onDenegado = { this.finish() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permisosHandler.handle()
    }
}
