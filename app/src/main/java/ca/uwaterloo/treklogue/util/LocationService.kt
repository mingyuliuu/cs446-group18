package ca.uwaterloo.treklogue.util
import android.Manifest
import android.R
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import ca.uwaterloo.treklogue.MainActivity
import com.google.android.gms.location.*

/**
 * Starts location updates on background and publish LocationUpdateEvent upon
 * each new location result.
 */
class LocationUpdateService : Service() {
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private val locationSettingsRequest: LocationSettingsRequest? = null

    //endregion
    //onCreate
    override fun onCreate() {
        super.onCreate()
        initData()
    }


    //Location Callback
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val currentLocation = locationResult.lastLocation
            Log.d("Locations", currentLocation!!.latitude.toString() + "," + currentLocation.longitude)
            //Share/Publish Location
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        prepareForegroundNotification()
        startLocationUpdates()

        return START_STICKY
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mFusedLocationClient!!.requestLocationUpdates(
            locationRequest!!,
            this.locationCallback, Looper.myLooper()
        )
    }

    private fun prepareForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "1",
                "Location Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            1,
            notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, "1")
            .setContentTitle("uhhhh")
//            .setContentTitle("?")
//            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun initData() {
        locationRequest = LocationRequest.create()
        locationRequest!!.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(baseContext)
    }

    companion object {
        //region data
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 3000
    }
}