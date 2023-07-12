package com.example.damusalama
//import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
//import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
//import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
//import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception
import java.util.*

class LocationActivity : AppCompatActivity(), OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener, View.OnClickListener {
    // variables for adding location layer
    private var mapView: MapView? = null
    private var client: MapboxDirections? = null
    private var mapboxMap: MapboxMap? = null
    private val bottomNavigationView: BottomNavigationView? = null
    private var back: Button? = null
    private var locate: CardView? = null

    // variables for adding location layer
    private var permissionsManager: PermissionsManager? = null
    private var locationComponent: LocationComponent? = null

    // variables for calculating and drawing a route
    private var currentRoute: DirectionsRoute? = null
    private val navigationMapRoute: NavigationMapRoute? = null

    // variables needed to initialize navigation
    private var button: Button? = null
    var point: LatLng? = null
    private val origin: Point? = null
    private val destination: Point? = null
    var myStyle: Style? = null
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_location)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        back = findViewById<Button>(R.id.back)
        back!!.setOnClickListener(this)
        val lat: String = getIntent().getStringExtra("latitude")
        val lon: String = getIntent().getStringExtra("longitude")
        val latitude = lat.toDouble()
        val longitude = lon.toDouble()
        point = LatLng(latitude, longitude)
        locate = findViewById<CardView>(R.id.locate)
        locate.setOnClickListener(this)
        //        onMapClick(point);
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.locate -> {
                locate.setVisibility(View.GONE)
                onMapClick(point)
            }
            R.id.back -> {
                startActivity(Intent(this@LocationActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(getString(R.string.mapbox_style_satellite_streets), object : OnStyleLoaded() {
            fun onStyleLoaded(style: Style) {
                enableLocationComponent(style)
                myStyle = style
                //                mapboxMap.addOnMapClickListener(LocationActivity.this);
                button = findViewById<Button>(R.id.startNavigating)
                button!!.setOnClickListener {
                    val simulateRoute = true
                    //                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
//                                .directionsRoute(currentRoute)
//                                .shouldSimulateRoute(false)
//                                .build();
//// Call this method with Context from within an Activity
//                        NavigationLauncher.startNavigation(LocationActivity.this, options);
                }
            }
        })
    }

    /**
     * Add the route and marker sources to the map
     */
    private fun initSource(loadedMapStyle: Style, originPoint: Point, destinationPoint: Point) {
        loadedMapStyle.addSource(GeoJsonSource(ROUTE_SOURCE_ID))
        val iconGeoJsonSource = GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(arrayOf(
                Feature.fromGeometry(Point.fromLngLat(originPoint.longitude(), originPoint.latitude())),
                Feature.fromGeometry(Point.fromLngLat(destinationPoint.longitude(), destinationPoint.latitude())))))
        loadedMapStyle.addSource(iconGeoJsonSource)
    }

    /**
     * Add the route and marker icon layers to the map
     */
    private fun initLayers(loadedMapStyle: Style) {
        val routeLayer = LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID)

// Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#FF3700B3"))
        )
        loadedMapStyle.addLayer(routeLayer)

// Add the red marker icon image to the map
        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.mapbox_marker_icon_default)))

// Add the red marker icon SymbolLayer to the map
        loadedMapStyle.addLayer(SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(RED_PIN_ICON_ID),
                iconIgnorePlacement(true),
                iconAllowOverlap(true
                ),
                iconOffset(arrayOf(0f, -9f))))
    }

    private fun addDestinationIconSymbolLayer(loadedMapStyle: Style) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default))
        val geoJsonSource = GeoJsonSource("destination-source-id")
        loadedMapStyle.addSource(geoJsonSource)
        val destinationSymbolLayer = SymbolLayer("destination-symbol-layer-id", "destination-source-id")
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        )
        loadedMapStyle.addLayer(destinationSymbolLayer)
    }

    fun onMapClick(point: LatLng): Boolean {
        //handle turning on location
        val lm: LocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder(this)
                    .setMessage("Please turn on Location to continue")
                    .setPositiveButton("Open Location Settings", object : DialogInterface.OnClickListener {
                        override fun onClick(paramDialogInterface: DialogInterface, paramInt: Int) {
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        }
                    }).setNegativeButton("Cancel", null)
                    .show()
        }
        //handle turning on location
        if (!gps_enabled && !network_enabled) {
//            Toast.makeText(this,"Location must be on",Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            val destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude())
            assert(locationComponent.getLastKnownLocation() != null)
            val originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                    locationComponent.getLastKnownLocation().getLatitude())
            val source: GeoJsonSource = Objects.requireNonNull(mapboxMap.getStyle()).getSourceAs("destination-source-id")
            if (source != null) {
                source.setGeoJson(Feature.fromGeometry(destinationPoint))
            }
            initSource(myStyle, originPoint, destinationPoint)
            initLayers(myStyle)
            getRoute(mapboxMap, originPoint, destinationPoint)
            button!!.isEnabled = true
            //            button.setTextColor(Color.WHITE);
//            button.setBackgroundResource(R.color.colorAccent2);
            return true
        }
        return false
    }

    //    private void getRoute(Point origin, Point destination) {
    //        assert Mapbox.getAccessToken() != null;
    //        NavigationRoute.builder(this)
    //                .accessToken(Mapbox.getAccessToken())
    //                .origin(origin)
    //                .destination(destination)
    //                .build()
    //                .getRoute(new Callback<DirectionsResponse>() {
    //                    @Override
    //                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
    //// You can get the generic HTTP info about the response
    //                        Log.d(TAG, "Response code: " + response.code());
    //                        if (response.body() == null) {
    //                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
    //                            return;
    //                        } else if (response.body().routes().size() < 1) {
    //                            Log.e(TAG, "No routes found");
    //                            return;
    //                        }
    //
    //                        currentRoute = response.body().routes().get(0);
    //
    //// Draw the route on the map
    //                        if (navigationMapRoute != null) {
    //                            navigationMapRoute.removeRoute();
    //                        } else {
    //                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.Animation_Design_BottomSheetDialog/*.mapbox_LocationLayer.mapbox_LocationComponent.NavigationLocationLayerStyle.NavigationMapRoute*/);
    //                        }
    //                        navigationMapRoute.addRoute(currentRoute);
    //                    }
    //
    //                    @Override
    //                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
    //                        Log.e(TAG, "Error: " + throwable.getMessage());
    //                    }
    //                });
    //    }
    @SuppressLint("WrongConstant")
    private fun enableLocationComponent(loadedMapStyle: Style) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
// Activate the MapboxMap LocationComponent to show user location
// Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap.getLocationComponent()
            locationComponent.activateLocationComponent(this, loadedMapStyle)
            locationComponent.setLocationComponentEnabled(true)
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING)
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

     fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show()
    }

     fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(Objects.requireNonNull(mapboxMap.getStyle()))
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    protected  fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    protected fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    protected fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    protected fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    protected fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    protected fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun getRoute(mapboxMap: MapboxMap?, origin: Point, destination: Point) {
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getString(R.string.access_token))
                .build()
        client.enqueueCall(object : Callback<DirectionsResponse?> {
            override fun onResponse(call: Call<DirectionsResponse?>, response: Response<DirectionsResponse?>) {
// You can get the generic HTTP info about the response
                Timber.d("Response code: " + response.code())
                if (response.body() == null) {
                    Timber.e("No routes found, make sure you set the right user and access token.")
                    return
                } else if (response.body().routes().size < 1) {
                    Timber.e("No routes found")
                    return
                }

// Get the directions route
                currentRoute = response.body().routes().get(0)

// Make a toast which displays the route's distance
//                Toast.makeText(LocationActivity.this, String.format(
//                        getString(R.string.directions_activity_toast_message),
//                        currentRoute.distance()), Toast.LENGTH_SHORT).show();
                if (mapboxMap != null) {
                    mapboxMap.getStyle(object : OnStyleLoaded() {
                        fun onStyleLoaded(style: Style) {

// Retrieve and update the source designated for showing the directions route
                            val source: GeoJsonSource = style.getSourceAs(ROUTE_SOURCE_ID)

// Create a LineString with the directions route's geometry and
// reset the GeoJSON source for the route LineLayer source
                            if (source != null) {
                                source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), Constants.PRECISION_6))
                                //                                source.setGeoJson(FeatureCollection.fromJson(""));
                            }
                        }
                    })
                }
            }

            override fun onFailure(call: Call<DirectionsResponse?>, throwable: Throwable) {
                Timber.e("Error: " + throwable.message)
                Toast.makeText(this@LocationActivity, "Error: " + throwable.message,
                        Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private const val ROUTE_SOURCE_ID = "route-source-id"
        private const val ROUTE_LAYER_ID = "route-layer-id"
        private const val ICON_LAYER_ID = "icon-layer-id"
        private const val ICON_SOURCE_ID = "icon-source-id"
        private const val RED_PIN_ICON_ID = "red-pin-icon-id"
        private const val TAG = "DirectionsActivity"
    }

private fun AlertDialog.Builder.setPositiveButton(s: String, any: Any) {

}
}
