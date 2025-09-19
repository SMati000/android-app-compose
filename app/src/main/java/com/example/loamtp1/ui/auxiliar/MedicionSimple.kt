package com.example.loamtp1.ui.auxiliar

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlin.math.sqrt

@Composable
fun MedicionSimple() {
    val lifecycleOwner = LocalLifecycleOwner.current
    var points by remember { mutableStateOf(listOf<Offset>()) }
    var referenceLength by remember { mutableStateOf("") }
    var referencePixels by remember { mutableStateOf<Float?>(null) }
    var measuredDistance by remember { mutableStateOf<Float?>(null) }
    var isCalibrationMode by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {

        // Vista de la cámara
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PreviewView(ctx).apply {
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(surfaceProvider)
                        }
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                }
            }
        )

        // Canvas para dibujar puntos y líneas
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (points.size >= 2) {
                            points = emptyList()
                            if (!isCalibrationMode) {
                                measuredDistance = null
                            }
                        }
                        points = points + offset

                        if (points.size == 2) {
                            val distance = calcularDistanciaPixeles(points[0], points[1])
                            if (isCalibrationMode) {
                                referencePixels = distance
                            } else if (referencePixels != null && referenceLength.isNotEmpty()) {
                                val refLength = referenceLength.toFloatOrNull() ?: 1f
                                val realDistance = (distance * refLength) / referencePixels!!
                                measuredDistance = realDistance
                            }
                        }
                    }
                }
        ) {
            dibujarPuntos(points)
            if (points.size == 2) {
                dibujarLinea(points[0], points[1])
            }
        }

        // Panel de control
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (isCalibrationMode) {
                VistaCalibracion(
                    referenceLength = referenceLength,
                    onReferenceLengthChange = { referenceLength = it },
                    referencePixels = referencePixels,
                    onCalibrationComplete = {
                        if (referencePixels != null && referenceLength.isNotEmpty()) {
                            isCalibrationMode = false
                        }
                    }
                )
            } else {
                VistaMedir(
                    distance = measuredDistance,
                    onRecalibrate = {
                        isCalibrationMode = true
                        referencePixels = null
                        referenceLength = ""
                        points = emptyList()
                        measuredDistance = null
                    }
                )
            }
        }
    }
}

@Composable
fun VistaCalibracion(
    referenceLength: String,
    onReferenceLengthChange: (String) -> Unit,
    referencePixels: Float?,
    onCalibrationComplete: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "1. Calibración",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Mide un objeto de longitud conocida",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = referenceLength,
                onValueChange = onReferenceLengthChange,
                label = { Text("Longitud real (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onCalibrationComplete,
                enabled = referencePixels != null && referenceLength.isNotEmpty()
            ) {
                Text("Listo")
            }
        }

        referencePixels?.let {
            Text(
                text = "Píxeles medidos: ${"%.1f".format(it)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun VistaMedir(
    distance: Float?,
    onRecalibrate: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "2. Medición",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = distance?.let {
                "Distancia: ${"%.1f".format(it)} cm"
            } ?: "Toca dos puntos para medir",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Button(
            onClick = onRecalibrate,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Recalibrar")
        }
    }
}

private fun DrawScope.dibujarPuntos(points: List<Offset>) {
    points.forEachIndexed { index, point ->
        val color = if (index == 0) Color.Red else Color.Green
        drawCircle(
            color = color,
            radius = 15f,
            center = point
        )
    }
}

private fun DrawScope.dibujarLinea(start: Offset, end: Offset) {
    drawLine(
        color = Color.Blue,
        start = start,
        end = end,
        strokeWidth = 5f
    )
}

private fun calcularDistanciaPixeles(p1: Offset, p2: Offset): Float {
    return sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y))
}
