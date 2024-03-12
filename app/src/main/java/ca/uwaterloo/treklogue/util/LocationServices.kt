package ca.uwaterloo.treklogue.util

import android.annotation.SuppressLint
import android.content.Context
import ca.uwaterloo.treklogue.data.mockModel.MockLandmark
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.maps.model.LatLng
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@SuppressLint("MissingPermission")
fun getCurrentLocation(
    context: Context,
    callback: (Double, Double) -> Unit,
    priority: Boolean = true
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
    else Priority.PRIORITY_BALANCED_POWER_ACCURACY

    fusedLocationClient.getCurrentLocation(
        accuracy, CancellationTokenSource().token,
    ).addOnSuccessListener { location ->
        if (location != null) {
            val lat = location.latitude
            val long = location.longitude
            callback(lat, long)
        }
    }.addOnFailureListener { exception ->
        exception.printStackTrace()
    }
}

// from https://mapsplatform.google.com/resources/blog/how-calculate-distances-map-maps-javascript-api/
// return value is in kilometres
fun distance(userLoc : LatLng, landmark : MockLandmark): Double {
    val r = 6371.0710 // Radius of the Earth in kilometres
    val rlat1 = userLoc.latitude * (Math.PI/180) // Convert degrees to radians
    val rlat2 = landmark.latitude* (Math.PI/180) // Convert degrees to radians
    val difflat = rlat2-rlat1 // Radian difference (latitudes)
    val difflon = (landmark.longitude-userLoc.longitude) * (Math.PI/180) // Radian difference (longitudes)

    var d = 2 * r * asin(sqrt(sin(difflat/2) * sin(difflat/2) + cos(rlat1) * cos(rlat2) * sin(difflon/2) * sin(difflon/2)))
    return d
}