package com.michael.proverbs.feature.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.michael.easylog.logInline
import com.michael.proverbs.core.common.displayToast
import java.util.Calendar

fun scheduleDailyAlarm(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            // This intent typically opens the "Alarms & reminders" page for your app
            // You can then prompt the user to allow exact alarms.
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            return
        }
    }

    // Create an explicit intent for DailyAlarmReceiver
    val intent = Intent(context, DailyAlarmReceiver::class.java).apply {
        action = DailyAlarmReceiver.ACTION_DAILY_ALARM
    }

    if (canScheduleExactAlarms(context)) {
        // PendingIntent that will fire when the alarm is triggered
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0, // requestCode
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Calculate the trigger time for the next occurrence of 7:30 AM
        val triggerTimeInMillis = getNextAlarmTimeInMillis(7, 30)
//         Set trigger time 2 minutes from now
//        val triggerTimeInMillis = System.currentTimeMillis() + 1 * 60 * 1000

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeInMillis,
                pendingIntent
            )

        } catch (se: SecurityException) {
            displayToast(context, "SecurityException: ${se.message}")
            // The system denied exact alarm permissions
            // Fallback or prompt user to grant permission
        }

    }
}

/**
 * Returns the epoch milliseconds for the next specified [hour] and [minute].
 * If that time has already passed for today, it schedules for tomorrow.
 */
private fun getNextAlarmTimeInMillis(hour: Int, minute: Int): Long {
    // Start from current time
    val now = Calendar.getInstance()

    // Set the target time
    val target = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    // If the target is before "now", add 1 day
    if (target.before(now)) {
        target.add(Calendar.DAY_OF_MONTH, 1)
    }

    return target.timeInMillis
}

fun canScheduleExactAlarms(context: Context): Boolean {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        alarmManager.canScheduleExactAlarms()
    } else {
        // On Android 11 (API 30) and below, no special permission needed
        true
    }
}
