package com.example.damusalama

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import android.os.Bundle
import com.example.damusalama.R
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.android.gms.tasks.OnCompleteListener
import android.widget.Toast
import android.content.Intent
import com.example.damusalama.HomeMActivity
import com.google.android.gms.location.LocationRequest
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import com.example.damusalama.CaptureLocation
import android.location.LocationManager
import android.provider.Settings
import java.util.*

class CaptureLocation : AppCompatActivity() {
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var group: String? = null
    var phone: String? = null
    var location: String? = null
    var latitude = 0.0
    var longitude = 0.0
    var n: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_location)
        group = intent.getStringExtra("group")
        phone = intent.getStringExtra("phone")
        location = intent.getStringExtra("location")
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        lastLocation
    }

    private fun sendData() {
        Objects.requireNonNull(FirebaseAuth.getInstance().uid)?.let {
            FirebaseFirestore.getInstance().collection("Profile").document(it).addSnapshotListener { value, error ->
            n = value!!["number"].toString()
            val la = latitude.toString()
            val lo = longitude.toString()
            val request = HashMap<String, Any?>()
            request["group"] = group
            request["phone"] = phone
            request["n"] = n
            request["location"] = location
            request["latitude"] = la
            request["longitude"] = lo
            FirebaseFirestore.getInstance().collection("Request/Patient/$group").add(request).addOnCompleteListener {
                Toast.makeText(this@CaptureLocation, "Request Sent", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@CaptureLocation, HomeMActivity::class.java))
                finish()
            }
        }
        }
    }// if permissions aren't available,

    // request for permissions
//                            displayLocation.setText("coordinates: " + location.getLatitude() + "," + location.getLongitude());// getting last
    // location from
    // FusedLocationClient
    // object
// check if location is enabled
    // check if permissions are given
    @get:SuppressLint("MissingPermission")
    private val lastLocation: Unit
        private get() {
            // check if permissions are given
            if (checkPermissions()) {

                // check if location is enabled
                if (isLocationEnabled) {

                    // getting last
                    // location from
                    // FusedLocationClient
                    // object
                    mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
                        val location = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            latitude = location.latitude
                            longitude = location.longitude
                            //                            displayLocation.setText("coordinates: " + location.getLatitude() + "," + location.getLongitude());
                            sendData()
                        }
                    }
                } else {
                    Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            } else {
                // if permissions aren't available,
                // request for permissions
                requestPermissions()
            }
        }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        @SuppressLint("SetTextI18n")
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
            //            displayLocation.setText("coordinates: " + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
        }
    }

    // method to check for permissions
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
    }

    // method to check
    // if location is enabled
    private val isLocationEnabled: Boolean
        private get() {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }

    // If everything is alright then
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lastLocation
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION = 5
    }
}