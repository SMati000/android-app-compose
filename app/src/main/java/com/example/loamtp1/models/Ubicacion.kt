package com.example.loamtp1.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loamtp1.usecases.GuardarUbicacion
import com.example.loamtp1.usecases.GuardarUbicacionFirebase
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import java.text.SimpleDateFormat
import java.util.Locale

data class Ubicacion(
    val referencia: String,
    val coordenadas: GeoPoint
)

class UbicacionViewModel : ViewModel() {
    private val guardarUbicacion : GuardarUbicacion = GuardarUbicacionFirebase() // TODO: Esto se deberia inyectar como dependencia

    fun guardarUbicacion(punto: GeoPoint) {
        val referencia = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US)
            .format(System.currentTimeMillis())

        viewModelScope.launch {
            guardarUbicacion.guardar(ubicacion = Ubicacion(referencia, punto))
        }
    }
}
