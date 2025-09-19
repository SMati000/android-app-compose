package com.example.loamtp1.ui.auxiliar

import android.annotation.SuppressLint
import android.os.Environment
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.video.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.camera.view.PreviewView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/*
* LifecycleOwner is an interface that marks a class as something that has a lifecycle.
* Something that Android knows how to start/stop/kill.
* Activity and Fragment both implement LifecycleOwner.
*/
@SuppressLint("MissingPermission")
@Composable
fun PantallaCamara(tipoCamara: String, lifecycleOwner: LifecycleOwner) {
    val contexto = LocalContext.current
    var capturaVideo by remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    var grabacion by remember { mutableStateOf<Recording?>(null) }

    val vistaPrevia = remember { PreviewView(contexto) }

    /*
    * Ejecutar una corrutina dentro de un composable, vinculada al ciclo de vida del composable
    * key1=Unit hace que la corrutina se ejecute cuando el composable se genera por primera vez
    */
    LaunchedEffect(Unit) {
        val proveedorCamara = ProcessCameraProvider.getInstance(contexto).get()

        val selectorCamara = when (tipoCamara) {
            "FRONT" -> CameraSelector.DEFAULT_FRONT_CAMERA
            else -> CameraSelector.DEFAULT_BACK_CAMERA
        }

        val previa = Preview.Builder().build().also {
            it.setSurfaceProvider(vistaPrevia.surfaceProvider)
        }

        val grabador = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HD))
            .build()
        capturaVideo = VideoCapture.withOutput(grabador)

        try {
            proveedorCamara.unbindAll()
            proveedorCamara.bindToLifecycle(
                lifecycleOwner,
                selectorCamara,
                previa,
                capturaVideo
            )
        } catch (e: Exception) {
            Toast.makeText(contexto, "Error cámara: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Preview full-screen
        AndroidView(
            factory = { vistaPrevia },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        // Botón grabar
        Button(
            onClick = {
                val vc = capturaVideo ?: return@Button
                if (grabacion == null) {
                    val nombreArchivo = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US)
                        .format(System.currentTimeMillis()) + ".mp4"

                    val picturesDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ingmobil")
                    val archivo = File(picturesDir, nombreArchivo)

                    val opcionesSalida = FileOutputOptions.Builder(archivo).build()

                    grabacion = vc.output
                        .prepareRecording(contexto, opcionesSalida)
                        .apply { withAudioEnabled() }
                        .start(ContextCompat.getMainExecutor(contexto)) { event ->
                            if (event is VideoRecordEvent.Finalize) {
                                Toast.makeText(contexto, "Video guardado: ${archivo.absolutePath}", Toast.LENGTH_SHORT).show()
                                grabacion = null
                            }
                        }
                } else {
                    grabacion?.stop()
                    grabacion = null
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(if (grabacion == null) "Grabar" else "Detener y Guardar")
        }
    }
}

