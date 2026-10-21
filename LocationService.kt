package nl.rijmaatje.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder

/**
 * Starter foreground service. It keeps a native location session alive after the
 * UI requests it; UI-to-service controls and background speech are future work.
 */
class LocationService : Service(), LocationListener {
    private lateinit var manager: LocationManager

    override fun onCreate() {
        super.onCreate()
        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        createChannel()
        startForeground(42, notification("GPS-service klaar"))
        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, this)
        } catch (_: SecurityException) {
            stopSelf()
        } catch (_: Exception) {
            stopSelf()
        }
    }

    override fun onLocationChanged(location: Location) {
        val kmh = if (location.hasSpeed()) location.speed * 3.6f else 0f
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(42, notification("GPS actief · ${kmh.toInt()} km/u"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int =
        START_STICKY

    override fun onDestroy() {
        try { manager.removeUpdates(this) } catch (_: Exception) {}
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                "rijmaatje_gps", "Rijmaatje GPS",
                NotificationManager.IMPORTANCE_LOW
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    private fun notification(text: String): Notification {
        val builder = if (Build.VERSION.SDK_INT >= 26)
            Notification.Builder(this, "rijmaatje_gps")
        else Notification.Builder(this)
        return builder
            .setContentTitle("Rijmaatje is actief")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .build()
    }
}
