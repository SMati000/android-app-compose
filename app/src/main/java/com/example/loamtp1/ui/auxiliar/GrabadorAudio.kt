package com.example.loamtp1.ui.auxiliar

import android.Manifest
import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun GrabadorAudio(
    modifier: Modifier = Modifier,
    contexto: Context,
    onDetener: () -> Unit
) {
    val grabadora = remember { Grabadora() }
    val colorBotones = 0xFF4E1359

    val permisos = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val permisosHandler = permisosParaComposable(
        permisos = permisos,
        onConcedidos = {
            grabadora.iniciarGrabacion(contexto)
        },
        onDenegado = { onDetener() })

    Button(
        onClick = {
            permisosHandler.handle()
        },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4E1359),
            contentColor = Color.White
        ),
        modifier = modifier.fillMaxWidth(0.9f),
        enabled = !grabadora.estaGrabando
    ) {
        Text("Iniciar")
    }

    Spacer(modifier = Modifier.width(5.dp))

    Button(
        onClick = { grabadora.pausarReanudar() },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(colorBotones),
            contentColor = Color.White
        ),
        modifier = modifier.fillMaxWidth(0.9f),
        enabled = grabadora.estaGrabando
    ) {
        Text(if (grabadora.estaPausado) "Reanudar" else "Pausar")
    }

    Spacer(modifier = Modifier.width(5.dp))

    Button(
        onClick = {
            if(grabadora.estaGrabando) {
                grabadora.detenerGrabacion()
            }

            onDetener()
        },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(colorBotones),
            contentColor = Color.White
        ),
        modifier = modifier.fillMaxWidth(0.9f),
    ) {
        Text(if (grabadora.estaGrabando) "Detener" else "Salir")
    }
}

class Grabadora {

    private var grabador: MediaRecorder? = null
    private var archivo: String? = null

    var estaGrabando by mutableStateOf(false)
        private set

    var estaPausado by mutableStateOf(false)
        private set

    fun iniciarGrabacion(contexto: Context) {
        if (estaGrabando) return

        val nombreArchivo = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US)
            .format(System.currentTimeMillis()) + ".mp3"
        val picturesDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "ingmobil")

        if(!picturesDir.exists()) {
            picturesDir.mkdir()
        }

        val archivo = File(picturesDir, nombreArchivo)

        grabador = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(archivo.absolutePath)
            prepare()
            start()
        }

        estaGrabando = true
        estaPausado = false
    }

    fun pausarReanudar() {
        if (!estaGrabando) return

        if (estaPausado) {
            grabador?.resume()
            estaPausado = false
        } else {
            grabador?.pause()
            estaPausado = true
        }
    }

    fun detenerGrabacion(): String? {
        if (!estaGrabando) return null

        grabador?.apply {
            stop()
            reset()
            release()
        }
        grabador = null

        estaGrabando = false
        estaPausado = false
        return archivo
    }
}
