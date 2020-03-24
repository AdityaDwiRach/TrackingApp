package com.adr.trackingapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.adr.trackingapp.BuildConfig.ACCESS_TOKEN
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
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.turf.TurfConstants
import com.mapbox.turf.TurfMeasurement
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alert_dialog_description.*
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), PermissionsListener, OnMapReadyCallback, OnCameraTrackingChangedListener {

    private lateinit var mapView: MapView
    private lateinit var permissionManager: PermissionsManager
    private lateinit var originLocation: Location
    private lateinit var locationComponent: LocationComponent

    private var isInTrackingMode = false
    private var locationEngine: LocationEngine? = null
    private var locationLayerPlugin: LocationLayerPlugin? = null
    private val callback = MainActivityCallback(this)
    private var historyTotalDistance = 0.0
    private var startTime = 0L
    private var stopTime = 0L
//    private var intervalString = ""

    var arrayCoordinate: ArrayList<Point> = ArrayList()
    lateinit var map: MapboxMap

    companion object {
        private const val INTERVAL_MILIS = 5000L
        private const val MAX_WAIT_TIME = INTERVAL_MILIS * 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, ACCESS_TOKEN)
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
//        { mapboxMap ->
//            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
//                //map is setup, and the style has loaded. now you can add data or make other map adjustment
//            }
//        }

        btn_start_service.setOnClickListener {
            initLocationEngine()
            startTime = Calendar.getInstance().timeInMillis
            Toast.makeText(this, "current date is ${getCurrentDate()}", Toast.LENGTH_SHORT).show()
        }

        btn_finish.setOnClickListener {
            //TODO alert dialog untuk ngasih title running nya
            //TODO store data to room sql
            //TODO data yang ada di dalam sql : KM, description, gambar (possible), current date, average speed
            
            alertDialogSaveData()
            captureScreenshot(findViewById(R.id.mapView))
            stopTime = Calendar.getInstance().timeInMillis

            val time = intervalTime(startTime, stopTime)
            val averageSpeed = getAverageSpeed(historyTotalDistance / 1000, time)
//            Toast.makeText(this, "interval time = $interval seconds", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.history -> {
                startActivity(Intent(this, HistoryRunningActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.map = mapboxMap

        map.setStyle(Style.MAPBOX_STREETS) {
            enableLocationComponent(it)

            fab_mainactivity.setOnClickListener {
//                Toast.makeText(this, "testing click", Toast.LENGTH_SHORT).show()
                drawLine()
            }

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

    private fun alertDialogSaveData(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("SAVE DATA")
        builder.setMessage("Do you want to save running data?")
        builder.setCancelable(true)

        builder.setPositiveButton("YES"){ _, _ -> alertDialogDescription() }
        builder.setNegativeButton("NO"){ dialog, _ -> dialog.cancel() }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun alertDialogDescription(){
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_dialog_description, null)
        builder.setView(dialogView)
        builder.setCancelable(true)
//
//        builder.setPositiveButton("SAVE"){ _, _ ->
//            //TODO save data to room sql
//            val description = et_alert_dialog_description.text.toString()
//        }
//        builder.setNegativeButton("CANCEL"){ dialog, _ -> dialog.cancel() }

        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun captureScreenshot(view: View): Bitmap {
        //TODO find method that aren't deprecated
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache(true)
        val bitmapRaw = Bitmap.createBitmap(view.drawingCache)
        val bitmapCropped = Bitmap.createBitmap(bitmapRaw,0,0,60,40)
        view.isDrawingCacheEnabled = false
        return bitmapCropped
        //TODO need test
    }

    private fun intervalTime(startTime: Long, stopTime: Long): Double{
        val intervalMilis = stopTime - startTime
        val seconds = TimeUnit.MILLISECONDS.toSeconds(intervalMilis)
        return seconds.toDouble()
    }

    private fun getAverageSpeed(distanceInMeter: Double, timeInSecond: Double): Double{
        return distanceInMeter / timeInSecond
    }

    private fun getCurrentDate(): String{
        val test = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance(3)
        return formatter.format(test)
    }

    private fun drawLine(){
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

            if (arrayCoordinateSize > 2){
                for (i in 0 until arrayCoordinateSize - 1){
                    distance = TurfMeasurement.distance(arrayCoordinate[i], arrayCoordinate[i + 1], TurfConstants.UNIT_KILOMETERS)
                    totalDistance += distance
                    Log.d(MainActivity::class.java.simpleName, "isi array list ${arrayCoordinate[i]}")
                }
            }

            historyTotalDistance = BigDecimal(totalDistance).setScale(2, RoundingMode.HALF_EVEN).toDouble()

            Log.d(MainActivity::class.java.simpleName, "is this empty?  ${arrayCoordinate.isEmpty()}")

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

//            initLocationEngine()

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

    private fun enableLocation(){
        if (PermissionsManager.areLocationPermissionsGranted(this)){
            //do some stuff
        } else {
            permissionManager = PermissionsManager(this)
            permissionManager.requestLocationPermissions(this)
        }
    }

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
