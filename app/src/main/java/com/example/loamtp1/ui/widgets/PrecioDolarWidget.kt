package com.example.loamtp1.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.loamtp1.MainActivity
import com.example.loamtp1.R
import com.example.loamtp1.usecases.FetchPrecioDolar
import com.example.loamtp1.usecases.FetchPrecioDolarAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MiWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val fetchPrecioDolar: FetchPrecioDolar = FetchPrecioDolarAPI()

        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.mi_widget_layout)
            views.setTextViewText(R.id.textoLabel, "Cargando...")
            views.setTextViewText(R.id.textoValor, "...")

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widgetContainer, pendingIntent)

            appWidgetManager.updateAppWidget(id, views)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val dolarOficial = fetchPrecioDolar.fetchPrecio()

                    val updatedViews = RemoteViews(context.packageName, R.layout.mi_widget_layout)
                    updatedViews.setTextViewText(R.id.textoLabel, dolarOficial.nombre)
                    updatedViews.setTextViewText(R.id.textoValor, "$${dolarOficial.valor}")
                    updatedViews.setOnClickPendingIntent(R.id.widgetContainer, pendingIntent)

                    appWidgetManager.updateAppWidget(id, updatedViews)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
