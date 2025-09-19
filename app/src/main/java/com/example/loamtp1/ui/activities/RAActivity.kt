package com.example.loamtp1.ui.activities

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.loamtp1.ui.auxiliar.DetectorDeCaras
import com.example.loamtp1.ui.auxiliar.permisosParaActivity

class RAActivity : ComponentActivity() {
    private val permisos = arrayOf(Manifest.permission.CAMERA)

    private val permisosHandler = permisosParaActivity(
        activity = this,
        permisos = permisos,
        onConcedidos = {
            setContent {
                DetectorDeCaras()
            }
        },
        onDenegado = { this.finish() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permisosHandler.handle()
    }
}
