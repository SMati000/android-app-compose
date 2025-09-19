package com.example.loamtp1.usecases

import com.example.loamtp1.models.Precio
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

interface FetchPrecios {
    suspend fun fetchPrecios(): List<Precio>
}

class FetchPreciosFirebase : FetchPrecios {
    private val db by lazy {
        FirebaseFirestore.getInstance()
    }

    override suspend fun fetchPrecios(): List<Precio> {
        return try {
            val result = db.collection("precios").get().await()
            result.documents.mapNotNull { doc ->
                val nombre = doc.getString("nombre") ?: return@mapNotNull null
                val valor = doc.getDouble("valor")?.toFloat() ?: return@mapNotNull null
                Precio(nombre, valor)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

class FetchPreciosPruebaLocal : FetchPrecios {
    override suspend fun fetchPrecios(): List<Precio> {
        return listOf(
            Precio("Honorarios hora", Random.nextDouble(3000.0, 15000.0).toFloat()),
            Precio("m2 Construccion tradicional", Random.nextDouble(3000.0, 15000.0).toFloat()),
            Precio("m2 Construccion en seco", Random.nextDouble(3000.0, 15000.0).toFloat()),
            Precio("m2 Construccion prefabricada", Random.nextDouble(3000.0, 15000.0).toFloat())
        )
    }
}
