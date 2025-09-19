package com.example.loamtp1.usecases

import com.example.loamtp1.models.Mensaje
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

interface Comunicador {
    fun enviarMensaje(texto: String, esUsuario: Boolean)

    // Flow es un stream asincrono que se puede recolectar en el tiempo
    fun recibirMensajes(): Flow<Mensaje>
}

class ComunicadorFirebase : Comunicador {
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun enviarMensaje(texto: String, esUsuario: Boolean) {
        val mensaje = hashMapOf(
            "texto" to texto,
            "esUsuario" to esUsuario,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("chat")
            .add(mensaje)
            .addOnFailureListener {
                // TODO
            }
    }

    override fun recibirMensajes(): Flow<Mensaje> = callbackFlow { // se ejecuta cuando se hace un collect
        val listener = db.collection("chat")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    return@addSnapshotListener
                }

                for (doc in snapshot.documentChanges) {
                    val texto = doc.document.getString("texto") ?: ""
                    val esUsuario = doc.document.getBoolean("esUsuario") ?: false
                    trySend(Mensaje(texto, esUsuario)) // agrega mensaje al Flow
                }
            }

        awaitClose { listener.remove() }
    }
}

class ComunicadorPruebaLocal : Comunicador {
    private val mensajes = mutableListOf<Mensaje>()
    private var listener: ((Mensaje) -> Unit)? = null

    override fun enviarMensaje(texto: String, esUsuario: Boolean) {
        val nuevoMensaje = Mensaje(texto, esUsuario)
        mensajes.add(nuevoMensaje)
        listener?.invoke(nuevoMensaje)

        // Respuesta automática
        if (esUsuario) {
            listener?.let { callback ->
                // Simula una demora antes de responder
                kotlinx.coroutines.GlobalScope.launch {
                    kotlinx.coroutines.delay(1000)
                    val respuesta = Mensaje("Respuesta a: $texto", false)
                    mensajes.add(respuesta)
                    callback(respuesta)
                }
            }
        }
    }

    override fun recibirMensajes(): Flow<Mensaje> = callbackFlow {
        listener = { msg -> trySend(msg) }
        mensajes.forEach { trySend(it) }

        awaitClose {
            listener = null
        }
    }
}
