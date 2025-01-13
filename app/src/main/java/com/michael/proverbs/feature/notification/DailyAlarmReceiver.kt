package com.michael.proverbs.feature.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.michael.easylog.logInlineNullable
import com.michael.easylog.logToFile
import com.michael.proverbs.R
import com.michael.proverbs.core.di.DailyAlarmReceiverEntryPoint
import com.michael.proverbs.feature.entrypoint.MainActivity
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DailyAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        // This will be called when the Alarm triggers at ~7:30 AM
        if (intent?.action == ACTION_DAILY_ALARM) {
            // Show your notification directly, or enqueue a Worker

            val hiltEntryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                DailyAlarmReceiverEntryPoint::class.java
            )
            val repository = hiltEntryPoint.proverbsRepository()

            CoroutineScope(Dispatchers.IO).launch {
                val verse = repository.getChapters().first().random()?.verses?.random()
                showNotification(context, verse?.chapterNumber.toString(), verse?.verseNumber.toString(), verse?.verseText.orEmpty())
            }

            scheduleDailyAlarm(context)
//            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>().build()
//            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }



    companion object {
        const val ACTION_DAILY_ALARM = "com.proverbs.daily.ALARM"
        const val NOTIFICATION_ID = 1001
    }
}

fun showNotification(context: Context, chapterNumber: String, verseNumber: String, verseText: String) {
    val channelId = "daily_notification_channel"
    val title = "Proverbs ${chapterNumber}: $verseNumber"


    val notificationIntent = Intent(context, MainActivity::class.java).apply {
        // If you have a single-activity app with multiple fragments or Compose screens,
        // you might handle the "message" inside MainActivity's onCreate or your navigation logic.
        putExtra("VERSE_TEXT", verseText)
        putExtra("CHAPTER_NUMBER", chapterNumber)
        putExtra("VERSE_NUMBER", verseNumber)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    // Wrap it in a PendingIntent
    val pendingIntent = PendingIntent.getActivity(
        context,
        0, // requestCode
        notificationIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Create a notification channel for Android O+
    createNotificationChannel(context, channelId, "Daily Notification", "Channel for daily notifications")

    // Build the notification
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.quotes_svgrepo_com) // your own icon
        .setContentTitle(title)
        .setContentText(verseText)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)

    // Show the notification
    if (ActivityCompat.checkSelfPermission(
            context,
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
    NotificationManagerCompat.from(context).notify(DailyAlarmReceiver.NOTIFICATION_ID, builder.build())
}

private fun createNotificationChannel(
    context: Context,
    channelId: String,
    name: String,
    description: String
) {
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(channelId, name, importance).apply {
        this.description = description
    }
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}