package com.adr.trackingapp

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.util.Property
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.PropertyValue
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.turf.TurfConstants
import com.mapbox.turf.TurfMeasurement
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), PermissionsListener, OnMapReadyCallback, OnCameraTrackingChangedListener {

    private lateinit var mapView: MapView
    lateinit var map: MapboxMap
    private lateinit var permissionManager: PermissionsManager
    private lateinit var originLocation: Location
    private lateinit var locationComponent: LocationComponent
    private var isInTrackingMode = false

    private var locationEngine: LocationEngine? = null
    private var locationLayerPlugin: LocationLayerPlugin? = null
    var arrayCoordinate: ArrayList<Point> = ArrayList()
    private val callback = MainActivityCallback(this)

    companion object {
        private const val INTERVAL_MILIS = 5000L
        private const val MAX_WAIT_TIME = INTERVAL_MILIS * 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, "pk.eyJ1IjoiYWRpdHlhZHIiLCJhIjoiY2s3dHd4ZTJuMTQ5YTNtcjJ5cnZ2anB4YyJ9.j2rKwfs-x2Qe7j3UYqX8HA")
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
//        { mapboxMap ->
//            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
//                //map is setup, and the style has loaded. now you can add data or make other map adjustment
//            }
//        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.map = mapboxMap

        map.setStyle(Style.MAPBOX_STREETS) {
            enableLocationComponent(it)

            fab_mainactivity.setOnClickListener {
//                Toast.makeText(this, "testing click", Toast.LENGTH_SHORT).show()
                testing()
            }
//            it.addSource(GeoJsonSource("line-source", FeatureCollection.fromFeatures(
//                arrayOf(Feature.fromGeometry(LineString.fromLngLats(arrayCoordinate))))))
//
//            it.addLayer(LineLayer("linelayer", "line-source").withProperties(
//                PropertyFactory.lineDasharray(arrayOf(0.01f, 2f)),
//                PropertyFactory.lineCap(com.mapbox.mapboxsdk.style.layers.Property.LINE_CAP_ROUND),
//                PropertyFactory.lineJoin(com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND),
//                PropertyFactory.lineWidth(5f),
//                PropertyFactory.lineColor(Color.RED)
//            ))

    //            style.addLayer(
    //                LineLayer("linelayer", "line-source").withProperties(
    //                    PropertyFactory.lineDasharray(
    //                        arrayOf(
    //                            0.01f,
    //                            2f
    //                        )
    //                    ),
    //                    PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
    //                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
    //                    PropertyFactory.lineWidth(5f),
    //                    PropertyFactory.lineColor(
    //                        Color.parseColor(
    //                            "#e55e5e"
    //                        )
    //                    )
    //                )
    //            )
        }
//            Style.Builder().fromUri("mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7"))
//        { enableLocationComponent(it) }
    }

    private fun testing(){
        map.setStyle(Style.MAPBOX_STREETS){
            it.addSource(GeoJsonSource("line-source", FeatureCollection.fromFeatures(
                arrayOf(Feature.fromGeometry(LineString.fromLngLats(arrayCoordinate))))))

            it.addLayer(LineLayer("linelayer", "line-source").withProperties(
                PropertyFactory.lineCap(com.mapbox.mapboxsdk.style.layers.Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(5f),
                PropertyFactory.lineColor(Color.RED)
            ))

            val arrayCoordinateSize = arrayCoordinate.size
            var distance = 0.0
            var totalDistance = 0.0

            if (arrayCoordinateSize >= 2){
                for (i in 0 until arrayCoordinateSize){
                    distance = TurfMeasurement.distance(arrayCoordinate[i], arrayCoordinate[i + 1], TurfConstants.UNIT_KILOMETERS)
                    totalDistance += distance
                }
            }

//            if (arrayCoordinateSize >= 2 && distance > 0){
//                totalDistance += distance
//            }
//            for (i in arrayCoordinate){
//
//            }

            Toast.makeText(this, "this is totalDistance : $totalDistance", Toast.LENGTH_SHORT).show()
        }
    }

    private fun enableLocationComponent(style: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)){

            //create and customize the locationcomponent options
            val customLocationComponentOptions = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .elevation(5F)
                .accuracyAlpha(.6F)
                .accuracyColor(Color.RED)
                .build()

            locationComponent = map.locationComponent

            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, style)
                .locationComponentOptions(customLocationComponentOptions)
                .build()

            //get instance of the locationcomponent and then adjust its settings
            map.locationComponent.apply {

                //activate thte locationcomponent with options
                activateLocationComponent(locationComponentActivationOptions)

                //enable to make the locationcomponent visible
                isLocationComponentEnabled = true

                //set the locationcomponent camera mode
                cameraMode = CameraMode.TRACKING

                //set the locationcomponent render mode
                renderMode = RenderMode.COMPASS

            }

            initLocationEngine()

            locationComponent.addOnCameraTrackingChangedListener(this)

//            fab_mainactivity.setOnClickListener {
//                if (!isInTrackingMode) {
//                    isInTrackingMode = true
//                    locationComponent.cameraMode = CameraMode.TRACKING
//                    locationComponent.zoomWhileTracking(16.0)
//                }
//            }
        } else {
            permissionManager = PermissionsManager(this)
            permissionManager.requestLocationPermissions(this)
        }
    }

    override fun onCameraTrackingChanged(currentMode: Int) {
    }

    override fun onCameraTrackingDismissed() {
        isInTrackingMode = false
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
    private fun initLocationEngine(){
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        val request = LocationEngineRequest.Builder(INTERVAL_MILIS)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(MAX_WAIT_TIME)
            .build()

        locationEngine?.requestLocationUpdates(request, callback, Looper.getMainLooper())
        locationEngine?.getLastLocation(callback)
//        val lastLocation = locationEngine?.getLastLocation()
//        if (lastLocation != null){
//            originLocation = lastLocation
//        }
    }

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
        if (locationEngine != null){
            locationEngine?.removeLocationUpdates(callback)
        }
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
}

private class MainActivityCallback(activity: MainActivity): LocationEngineCallback<LocationEngineResult>{
    private val activityWeakReference: WeakReference<MainActivity> = WeakReference(activity)

    override fun onFailure(exception: Exception) {
        val activity = activityWeakReference.get()
        if (activity != null){
            Toast.makeText(activity, exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSuccess(result: LocationEngineResult?) {
        val activity = activityWeakReference.get()
        if (activity != null){
            val location = result?.lastLocation ?: return

            if (result.lastLocation != null){
                val longitude = result.lastLocation?.longitude!!.toDouble()
                val latitude = result.lastLocation?.latitude!!.toDouble()
                Log.d(MainActivity::class.java.simpleName, "longitude : $longitude, latitude : $latitude")
                activity.arrayCoordinate.add(Point.fromLngLat(longitude, latitude))
            }
        }
    }

}
