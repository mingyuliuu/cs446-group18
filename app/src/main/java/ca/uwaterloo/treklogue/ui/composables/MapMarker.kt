package ca.uwaterloo.treklogue.ui.composables

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapMarker(
    context: Context,
    position: LatLng,
    title: String,
    @DrawableRes iconResourceId: Int,
    variant: String = "small" // "small" (default) or "large"
) {
    val icon = bitmapDescriptorFromVector(
        context, iconResourceId, variant
    )
    Marker(
        state = MarkerState(position = position),
        title = title,
        icon = icon
    )
}

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int,
    variant: String
): BitmapDescriptor? {
    val height = if (variant == "small") 180 else 220
    val width = if (variant == "small") 180 else 220

    // Retrieve the actual drawable (scaled)
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    val bitmap = drawable.toBitmap(width, height)

    // Draw it onto the bitmap
    val canvas = android.graphics.Canvas(bitmap)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}