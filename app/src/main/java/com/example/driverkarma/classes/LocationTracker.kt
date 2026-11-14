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

class LocationTracker(context: Context) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locations = mutableListOf<Location>()

    private var locationCallback: LocationCallback? = null

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

    fun stopTracking() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }

        locationCallback = null
    }

    fun getLocations(): List<Location> {
        synchronized(locations) {
            return locations.toList()
        }
    }
}