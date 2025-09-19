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

/*
* ViewModel es un componente de Compose
* Consciente del ciclo de vida y sobrevive a los cambios de configuración
* Mantiene el estado de la interfaz y la lógica para que no se recree junto con el activity o composable
* Permite instanciar una instancia única dentro del ViewModelStoreOwner actual con el helper viewModel().
*/
class ChatViewModel : ViewModel() {
    private val comunicador : Comunicador =  // TODO: Esto se deberia inyectar como dependencia
         ComunicadorFirebase()
//         ComunicadorPruebaLocal()

    private val _mensajes = mutableStateListOf<Mensaje>()
    val mensajes: List<Mensaje> get() = _mensajes

    var input by mutableStateOf("")
        private set

    init {
        /*
        * viewModelScope es un CoroutineScope vinculado al ciclo de vida del ViewModel
        * launch empieza una nueva corrutina asincrona en ese viewModelScope
        */
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
