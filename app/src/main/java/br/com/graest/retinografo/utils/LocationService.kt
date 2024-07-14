package br.com.graest.retinografo.utils


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationService(private val context: Context) {

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    fun getLocation(activity: Activity, onLocationReceived: (Location?) -> Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            requestLocation(onLocationReceived)
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int, grantResults: IntArray, onLocationReceived: (Location?) -> Unit
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                requestLocation(onLocationReceived)
            } else {
                // Permission denied, handle as needed
                onLocationReceived(null)
            }
        }
    }

    private fun requestLocation(onLocationReceived: (Location?) -> Unit) {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                onLocationReceived(location)
                locationManager.removeUpdates(this)  // Stop further updates once location is received
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not already granted
            onLocationReceived(null)
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 0L, 0f, locationListener
        )
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
