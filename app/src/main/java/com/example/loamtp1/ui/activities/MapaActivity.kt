package com.example.loamtp1.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.loamtp1.models.UbicacionViewModel
import com.example.loamtp1.ui.auxiliar.permisosParaActivity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.File

class MapaActivity : AppCompatActivity() {

    private lateinit var mapa: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationMarker: Marker? = null
    private var ubicacionCallback: LocationCallback? = null

    private val ubicacionViewModel: UbicacionViewModel by viewModels()

    private val permisos = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val permisosHandler = permisosParaActivity(
        activity = this,
        permisos = permisos,
        onConcedidos = {
            mostrarUbicacionUsuario()
        },
        onDenegado = { this.finish() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(
            this,
            PreferenceManager.getDefaultSharedPreferences(this)
        )
        Configuration.getInstance().userAgentValue = packageName
        Configuration.getInstance().osmdroidBasePath = filesDir
        Configuration.getInstance().osmdroidTileCache = File(filesDir, "osmdroid")

        mapa = MapView(this)
        mapa.setMultiTouchControls(true)
        mapa.setTileSource(TileSourceFactory.MAPNIK)
        mapa.controller.setCenter(GeoPoint(-34.6037, -58.3816))
        mapa.controller.setZoom(15.0)
        mapa.minZoomLevel = 3.0
        mapa.maxZoomLevel = 20.0

        val frameLayout = FrameLayout(this)

        frameLayout.addView(mapa, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ))

        frameLayout.fitsSystemWindows = true

        val boton = Button(this).apply {
            text = "Guardar Ubicacion"
            setOnClickListener {
                locationMarker?.position?.let { punto ->
                    ubicacionViewModel.guardarUbicacion(punto)
                    Toast.makeText(this@MapaActivity, "Ubicación guardada", Toast.LENGTH_SHORT).show()
                } ?: run {
                    Toast.makeText(this@MapaActivity, "Tu ubicacion no esta disponible", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.BOTTOM or Gravity.END
        params.setMargins(16,16,16,16)

        frameLayout.addView(boton, params)

        setContentView(frameLayout)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        permisosHandler.handle()
    }

    override fun onDestroy() {
        super.onDestroy()
        ubicacionCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
        mapa.onDetach() // liberar recursos de osmdroid
    }

    @SuppressLint("MissingPermission")
    private fun mostrarUbicacionUsuario() {
        val solicitudUbicacion = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setMinUpdateIntervalMillis(2000)
            .build()

        ubicacionCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val ubic = result.lastLocation

                if(ubic == null) {
                    Toast.makeText(this@MapaActivity, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
                    return;
                }

                if (mapa.overlays.isEmpty()) {
                    val punto = GeoPoint(ubic.latitude, ubic.longitude)
                    mapa.controller.animateTo(punto)
                    mapa.controller.setZoom(17.0)

                    locationMarker?.let { marker ->
                        mapa.overlays.remove(marker)
                    }

                    locationMarker = Marker(mapa).apply {
                        position = punto
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = "Tu ubicación"
                    }

                    mapa.overlays.add(locationMarker)
                }
            }
        }

        ubicacionCallback?.let {
            fusedLocationClient.requestLocationUpdates(
                solicitudUbicacion,
                it,
                mainLooper
            )
        }
    }
}

