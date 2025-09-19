package com.example.loamtp1.ui.auxiliar

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class PermisosHandler(
    private val permisos: Array<String>,
    private val checkSelf: (String) -> Int,
    private val onConcedidos: () -> Unit,
    private val launchRequest: () -> Unit,
) {
    fun handle() {
        val concedidos = permisos.all { checkSelf(it) == PackageManager.PERMISSION_GRANTED }
        if (concedidos) {
            onConcedidos()
        } else {
            launchRequest()
        }
    }
}

fun permisosParaActivity(
    activity: ComponentActivity,
    permisos: Array<String>,
    onConcedidos: () -> Unit,
    onDenegado: () -> Unit = {}
): PermisosHandler {
    val launcher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { result ->
        val concedidos = result.all { it.value }
        if (concedidos) {
            onConcedidos()
        } else {
            Toast.makeText(activity, "Permisos denegados", Toast.LENGTH_SHORT).show()
            onDenegado()
        }
    }

    return PermisosHandler(
        permisos = permisos,
        checkSelf = { perm -> ContextCompat.checkSelfPermission(activity, perm) },
        onConcedidos = onConcedidos,
        launchRequest = { launcher.launch(permisos) },
    )
}


@Composable
fun permisosParaComposable(
    permisos: Array<String>,
    onConcedidos: () -> Unit,
    onDenegado: () -> Unit = {}
): PermisosHandler {
    val contexto = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { result ->
        val concedidos = result.all { it.value }
        if (concedidos) {
            onConcedidos()
        } else {
            Toast.makeText(contexto, "Permisos denegados", Toast.LENGTH_SHORT).show()
            onDenegado()
        }
    }

    return PermisosHandler(
        permisos = permisos,
        checkSelf = { perm -> ContextCompat.checkSelfPermission(contexto, perm) },
        onConcedidos = onConcedidos,
        launchRequest = { launcher.launch(permisos) },
    )
}
