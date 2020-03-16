package com.adr.trackingapp

import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import java.lang.Exception

class MainActivity : AppCompatActivity(), PermissionsListener, LocationEngineCallback<LocationEngineResult> {

    private lateinit var mapView: MapView
    private lateinit var map: MapboxMap
    private lateinit var permissionManager: PermissionsManager
    private lateinit var originLocation: Location

    private var locationEngine: LocationEngine? = null
    private var locationLayerPlugin: LocationLayerPlugin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, "pk.eyJ1IjoiYWRpdHlhZHIiLCJhIjoiY2s3dHd4ZTJuMTQ5YTNtcjJ5cnZ2anB4YyJ9.j2rKwfs-x2Qe7j3UYqX8HA")
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                //map is setup, and the style has loaded. now you can add data or make other map adjustment
            }
        }
    }

//    private fun enableLocation(){
//        if (PermissionsManager.areLocationPermissionsGranted(this)){
//            //do some stuff
//        } else {
//            permissionManager = PermissionsManager(this)
//            permissionManager.requestLocationPermissions(this)
//        }
//    }
//
//    private fun initializeLocationEngine(){
//        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
//
//        val lastLocation = locationEngine?.getLastLocation()
//        if (lastLocation != null){
//            originLocation = lastLocation
//        }
//    }

//    private fun initializeLocationLayer(){
//
//    }
//
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        // present toast or dialog explaining why need to grant access.
    }

    override fun onPermissionResult(granted: Boolean) {
//        if (granted){
//            enableLocation()
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onSuccess(result: LocationEngineResult?) {
        TODO("Not yet implemented")
    }

    override fun onFailure(exception: Exception) {
        TODO("Not yet implemented")
    }
}
