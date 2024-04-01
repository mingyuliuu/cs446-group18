package ca.uwaterloo.treklogue.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import ca.uwaterloo.treklogue.data.model.Landmark
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@SuppressLint("MissingPermission")
fun getCurrentLocation(
    context: Context,
    callback: (Double, Double, Boolean) -> Unit,
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    var initial = true

    val locationRequest = LocationRequest.Builder(10000)
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            for (location in p0.locations){
                val lat = location.latitude
                val long = location.longitude
                callback(lat, long, initial)
            }

            initial = false
        }
    }

    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
}

// from https://mapsplatform.google.com/resources/blog/how-calculate-distances-map-maps-javascript-api/
// return value is in kilometres
fun distance(userLoc : LatLng, landmark : Landmark): Double {
    val r = 6371.0710 // Radius of the Earth in kilometres
    val rlat1 = userLoc.latitude * (Math.PI/180) // Convert degrees to radians
    val rlat2 = landmark.latitude* (Math.PI/180) // Convert degrees to radians
    val difflat = rlat2-rlat1 // Radian difference (latitudes)
    val difflon = (landmark.longitude-userLoc.longitude) * (Math.PI/180) // Radian difference (longitudes)

    val d = 2 * r * asin(sqrt(sin(difflat/2) * sin(difflat/2) + cos(rlat1) * cos(rlat2) * sin(difflon/2) * sin(difflon/2)))
    return d
}