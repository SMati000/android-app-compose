package com.example.loamtp1.usecases

import com.example.loamtp1.models.Precio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

interface FetchPrecioDolar {
    // las funciones suspend pueden pausarse y seguir mas tarde, sin bloquear el hilo donde esta ejecutandose
    suspend fun fetchPrecio(): Precio
}

class FetchPrecioDolarAPI : FetchPrecioDolar {

    private val client = OkHttpClient()

    override suspend fun fetchPrecio(): Precio {
        return withContext(Dispatchers.IO) {
            try {
                val url = "https://dolarapi.com/v1/dolares/oficial"
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        return@withContext Precio(nombre = "Dólar Oficial", valor = 0f)
                    }
                    val bodyString = response.body?.string() ?: ""

                    val json = JSONObject(bodyString)
                    val nombre = "Dolar Oficial"
                    val venta = json.optDouble("venta", Double.NaN)

                    Precio(nombre = nombre, valor = venta.toFloat())
                }
            } catch (e: Exception) {
                Precio(nombre = "Dólar Oficial", valor = 0f)
            }
        }
    }
}
