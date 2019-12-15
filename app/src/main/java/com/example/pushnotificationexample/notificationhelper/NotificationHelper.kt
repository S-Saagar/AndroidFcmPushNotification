package com.example.pushnotificationexample.notificationhelper

import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pushnotificationexample.BuildConfig
import com.example.pushnotificationexample.PushApp
import com.example.pushnotificationexample.R
import com.example.pushnotificationexample.broadcast_receiver.Alarm


/**
 * Helper class to manage notification channels, and create notifications.
 */
class NotificationHelper(ctx: Context) : ContextWrapper(ctx) {

    private var manager: NotificationManagerCompat? = null

    /**
     * Get the small icon for this app
     *
     * @return The small icon resource id
     */
    private val smallIcon: Int get() = android.R.drawable.stat_notify_chat


    /**
     * Initialize notification channel on app splash screen
     */
    fun initPrimaryChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan1 = NotificationChannel(PRIMARY_CHANNEL,
                    getString(R.string.noti_channel_default), NotificationManager.IMPORTANCE_HIGH)
            chan1.lightColor = Color.GREEN
            chan1.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            getManager()!!.createNotificationChannel(chan1)
        }
    }

    /**
     * Get a notification of type 1
     *
     *
     * Provide the builder rather than the notification it's self as useful for making notification
     * changes.
     *
     * @param title the title of the notification
     * @param body  the body text for the notification
     * @return the builder as it keeps a reference to the notification (since API 24)
     */
    fun getNotification(title: String, body: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, PRIMARY_CHANNEL)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(body)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
    }

    /**
     * Get a notification of type 1
     *
     *
     * Provide the builder rather than the notification it's self as useful for making notification
     * changes.
     *
     * @param title the title of the notification
     * @param body  the body text for the notification
     * @param pendingIntent the intent for open desired screen
     * @return the builder as it keeps a reference to the notification (since API 24)
     */
    fun getNotification(title: String, body: String, pendingIntent: PendingIntent): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, PRIMARY_CHANNEL)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(body)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
    }

    /**
     * Send a notification.
     *
     * @param notification The notification object
     */
    fun notify(notification: NotificationCompat.Builder) {
        val preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        val notiId = preferences.getInt("last_notification_id", 0)
        getManager()?.let { notificationManager ->
            notificationManager.notify(notiId, notification.build())
            preferences.edit().putInt("last_notification_id", notiId + 1).apply()
        }
    }

    /**
     * Get the notification manager.
     *
     *
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private fun getManager(): NotificationManagerCompat? {
        if (manager == null) {
            manager = NotificationManagerCompat.from(this)
        }
        return manager
    }

    companion object {
        const val PRIMARY_CHANNEL = "default"
    }

    /**
     * Set alarm for check notifications
     */
    fun setAlarm() {
        val am = PushApp.instance.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val i = Intent(PushApp.instance.applicationContext, Alarm::class.java)
        i.action = "${BuildConfig.APPLICATION_ID}.CHECK_NOTIFICATIONS"

        val receiver = PendingIntent.getBroadcast(PushApp.instance.applicationContext, 1001, i, PendingIntent.FLAG_UPDATE_CURRENT)

        // 30 Seconds
        val h = 1
        val m = 1
        val s = 30

        // 4 Minutes
//        val h = 1
//        val m = 4
//        val s = 60

        // 4 Hours
//        val h = 4
//        val m = 60
//        val s = 60
        // This fire alarm at every h hour m minute and s second and can be set to minimum 30 second
        val alarmDelay = (1000 * s * m * h).toLong() // Milli_sec * Second * Minute * hours
        val alarmTimeAtUTC = System.currentTimeMillis() + alarmDelay

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeAtUTC, receiver)
        } else {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmDelay, receiver)
        }
    }

    fun cancelAlarm() {
        val am = PushApp.instance.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val i = Intent(PushApp.instance.applicationContext, Alarm::class.java)
        i.action = "${BuildConfig.APPLICATION_ID}.CHECK_NOTIFICATIONS"

        val receiver = PendingIntent.getBroadcast(PushApp.instance.applicationContext, 1001, i, PendingIntent.FLAG_UPDATE_CURRENT)

        am.cancel(receiver)
    }
}