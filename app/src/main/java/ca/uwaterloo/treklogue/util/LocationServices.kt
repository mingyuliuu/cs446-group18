package ca.uwaterloo.treklogue.util

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

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
