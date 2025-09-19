package com.example.loamtp1.models

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class InfoBateria(
    val porcentaje: Int,
    val cargando: Boolean,
    val tiempoRestante: String
)

class BateriaViewModel(aplicacion: Application) : AndroidViewModel(aplicacion) {
    private val _infoBateria = MutableStateFlow(InfoBateria(porcentaje = 0, cargando = false, tiempoRestante = "0"))
    val infoBateria: StateFlow<InfoBateria> = _infoBateria

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return
            val nivel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val escala = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val porcentaje = if (nivel >= 0 && escala > 0) (nivel * 100 / escala.toFloat()).toInt() else 0

            val estado = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val cargando = estado == BatteryManager.BATTERY_STATUS_CHARGING ||
                    estado == BatteryManager.BATTERY_STATUS_FULL

            val minutosEstimados = (720 * (porcentaje / 100f)).toInt() // asume q una bateria al 100% dura 12h y el ritmo de descarga es lineal
            _infoBateria.value = InfoBateria(porcentaje, cargando, minutosAHora(minutosEstimados))
        }
    }

    init {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        getApplication<Application>().registerReceiver(receiver, filter)
    }

    override fun onCleared() {
        getApplication<Application>().unregisterReceiver(receiver)
        super.onCleared()
    }
}

private fun minutosAHora(totalMinutes: Int): String {
    val h = totalMinutes / 60
    val m = totalMinutes % 60
    return "${h}h ${m}min"
}

