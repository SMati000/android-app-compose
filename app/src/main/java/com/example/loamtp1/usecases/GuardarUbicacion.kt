package com.example.loamtp1.usecases

import com.example.loamtp1.models.Ubicacion
import com.google.firebase.firestore.FirebaseFirestore

interface GuardarUbicacion {
    suspend fun guardar(ubicacion: Ubicacion)
}

class GuardarUbicacionFirebase : GuardarUbicacion {
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }

    override suspend fun guardar(ubicacion: Ubicacion) {
        db.collection("ubicaciones")
            .add(ubicacion)
            .addOnFailureListener {
                // TODO
            }
    }
}
