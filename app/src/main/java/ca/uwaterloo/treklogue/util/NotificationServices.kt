package ca.uwaterloo.treklogue.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import ca.uwaterloo.treklogue.MainActivity
import ca.uwaterloo.treklogue.R

/**
 * Handles all operations related to [Notification].
 * Modified from https://github.com/android/user-interface-samples
 */
class NotificationHelper(private val context: Context) {

    companion object {
        /**
         * The notification channel for new landmarks
         */
        private const val CHANNEL_NEW_LANDMARKS = "new_landmarks"
    }

    private val notificationManager: NotificationManager =
        context.getSystemService() ?: throw IllegalStateException()

    fun setUpNotificationChannels() {
        if (notificationManager.getNotificationChannel(CHANNEL_NEW_LANDMARKS) == null) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_NEW_LANDMARKS,
                    context.getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "treklogue notifications"
                }
            )
        }
    }

    // TODO: bug - opening notification with app in background crashes the app
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    @WorkerThread
    fun showNotification(name: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_NEW_LANDMARKS)
            .setSmallIcon(R.drawable.traveler)
            .setContentTitle("You've reached $name!")
            .setContentText("Take a picture now to commemorate!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setTimeoutAfter(10000L)
        // id should be unique, but is fine for now; current implementation would overload user notifs
        // if we make other notification types, add timer delay to new landmarks channel
        notificationManager.notify(1, builder.build())
    }
    private fun dismissNotification(id: Long) {
        notificationManager.cancel(id.toInt())
    }
}

