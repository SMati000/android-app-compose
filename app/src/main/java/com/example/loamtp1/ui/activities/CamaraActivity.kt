package com.example.loamtp1.ui.activities

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.loamtp1.ui.auxiliar.PantallaCamara
import com.example.loamtp1.ui.auxiliar.permisosParaActivity

class CamaraActivity : ComponentActivity() {

    private val permisos = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val permisosHandler = permisosParaActivity(
        activity = this,
        permisos = permisos,
        onConcedidos = {
            setContent {
                PantallaCamara(
                    tipoCamara = intent.getStringExtra("CAMERA_TYPE") ?: "BACK",
                    lifecycleOwner = this
                )
            }
        },
        onDenegado = { this.finish() })

    /*
    * A Bundle is a key–value map that Android uses to pass small amounts of data around.
    * If this is the first time the Activity is created, the parameter will be null.
    * If the Activity is being re-created, Android gives you back a Bundle containing whatever
      you saved earlier in onSaveInstanceState.
    */
    override fun onCreate(estadoGuardado: Bundle?) {
        super.onCreate(estadoGuardado)
        permisosHandler.handle()
    }
}
