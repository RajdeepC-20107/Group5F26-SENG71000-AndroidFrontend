package com.example.driverkarma.classes

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

/**
 * Class that manages the starting, ending, and polling of location data.
 * @author  Rajdeep Chowdhury
 */
class LocationTracker(context: Context) {

    /**
     * Fused Location Provider client
     */
    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * List that stores the location data polled over time.
     */
    private val locations = mutableListOf<Location>()

    /**
     * Callback for Fused Location Provider client
     */
    private var locationCallback: LocationCallback? = null

    /**
     * Begins the polling of location data, so as long as the required permissions are granted.
     *
     * @author  Rajdeep Chowdhury
     */
    @RequiresPermission(
        allOf = [
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ]
    )
    fun startTracking() {
        synchronized(locations) {
            locations.clear()
        }

        val locationRequest =
            LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                1000L
            )
                .setMinUpdateIntervalMillis(500L)
                .build()

        locationCallback = object : LocationCallback() {

            override fun onLocationResult(
                locationResult: LocationResult
            ) {
                locationResult.locations.forEach { location ->

                    synchronized(locations) {
                        locations.add(Location(location))
                    }

                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    /**
     * Stops the tracking and polling of location data.
     *
     * @author  Rajdeep Chowdhury
     */
    fun stopTracking() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }

        locationCallback = null
    }

    /**
     * Returns the location points of the latest session.
     *
     * @author  Rajdeep Chowdhury
     */
    fun getLocations(): List<Location> {
        synchronized(locations) {
            return locations.toList()
        }
    }
}