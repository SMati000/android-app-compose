package com.example.loamtp1.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loamtp1.usecases.FetchPrecioDolar
import com.example.loamtp1.usecases.FetchPrecioDolarAPI
import com.example.loamtp1.usecases.FetchPrecios
import com.example.loamtp1.usecases.FetchPreciosFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Precio(
    val nombre: String = "",
    val valor: Float = 0f
)

class PreciosViewModel : ViewModel() {
    private val fetchPrecios: FetchPrecios =  // TODO: Esto se deberia inyectar como dependencia
        FetchPreciosFirebase()
//        FetchPreciosPruebaLocal()
    private val fetchPrecioDolar: FetchPrecioDolar = FetchPrecioDolarAPI() // TODO: Esto se deberia inyectar como dependencia
    private val _precios = MutableStateFlow<List<Precio>>(emptyList())
    val precios: StateFlow<List<Precio>> = _precios

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun refreshPrecios() {
        if (_isRefreshing.value) return

        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val listaDesdeDb = fetchPrecios.fetchPrecios()
                val precioDolar = fetchPrecioDolar.fetchPrecio()
                _precios.value = listaDesdeDb + precioDolar
            } catch (e: Exception) {
                // TODO
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    init {
        refreshPrecios()
    }
}
