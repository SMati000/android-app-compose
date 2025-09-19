package com.example.loamtp1.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loamtp1.usecases.Comunicador
import com.example.loamtp1.usecases.ComunicadorFirebase
import com.example.loamtp1.usecases.ComunicadorPruebaLocal
import kotlinx.coroutines.launch

data class Mensaje(
    val texto: String,
    val esUsuario: Boolean
)

class ChatViewModel : ViewModel() {
    private val comunicador : Comunicador =  // TODO: Esto se deberia inyectar como dependencia
         ComunicadorFirebase()
//         ComunicadorPruebaLocal()

    private val _mensajes = mutableStateListOf<Mensaje>()
    val mensajes: List<Mensaje> get() = _mensajes

    var input by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            comunicador.recibirMensajes().collect { msg ->
                _mensajes.add(msg)
            }
        }
    }

    fun onInputChange(nuevo: String) {
        input = nuevo
    }

    fun enviarMensaje() {
        if (input.isBlank()) return

        val texto = input
        comunicador.enviarMensaje(texto, esUsuario = true)

        input = ""
    }

    fun recibirMensaje(texto: String) {
        _mensajes.add(Mensaje(texto, esUsuario = false))
    }
}
