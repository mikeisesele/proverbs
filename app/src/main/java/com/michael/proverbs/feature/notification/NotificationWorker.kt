package com.michael.proverbs.feature.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.michael.proverbs.R
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

// MyNotificationWorker.kt
class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // 1) Show the notification
        showNotification()

        // 2) Schedule the next day’s notification
        scheduleNextNotification(applicationContext)

        return Result.success()
    }

    private fun showNotification() {
        val channelId = "daily_notification_channel"
        val title = "Daily Reminder"
        val text = "It's 7:30 AM! Time for your daily update."

        // Create notification channel (for Android O+)
        createNotificationChannel(channelId, "Daily Notification", "Channel for daily notifications")

        // Build the notification
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.quotes_svgrepo_com) // your own icon
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Show notification
        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    /**
     * Create the notification channel for O+ devices.
     */
    private fun createNotificationChannel(channelId: String, name: String, description: String) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance).apply {
            this.description = description
        }
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
    }
}


fun scheduleNextNotification(context: Context) {
    val now = LocalDateTime.now()
    val nextTarget = getNext7h30(now)
    val delayInMillis = ChronoUnit.MILLIS.between(now, nextTarget)

    val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}

/**
 * Returns a LocalDateTime corresponding to the next occurrence of 7:30 AM
 * from the given [now].
 */
private fun getNext7h30(now: LocalDateTime): LocalDateTime {
    val targetTime = LocalTime.of(7, 30) // 7:30 AM
    val todayTarget = LocalDateTime.of(now.toLocalDate(), targetTime)

    return if (now.isAfter(todayTarget)) {
        // 7:30 AM already passed today; schedule for tomorrow
        todayTarget.plusDays(1)
    } else {
        // 7:30 AM is later today
        todayTarget
    }
}
