package com.michael.proverbs.feature.notification

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.michael.easylog.logToFile
import com.michael.proverbs.core.di.DailyAlarmReceiverEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class SyncManager(private val appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        // Your sync logic here
        // Example: sync contacts, fetch data, etc.

        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            appContext.applicationContext,
            DailyAlarmReceiverEntryPoint::class.java
        )
        val repository = hiltEntryPoint.proverbsRepository()

        CoroutineScope(Dispatchers.IO).launch {
            val verse = repository.getChapters().first().random()?.verses?.random()
            showNotification(appContext, verse?.chapterNumber.toString(), verse?.verseNumber.toString(), verse?.verseText.orEmpty())
            logToFile(
                logMessage = verse.toString(),
                context = appContext,
                shouldDeleteExistingFile = true,
                fileName = "syncContacts"
            )
        }

        // Schedule the next sync
        scheduleNextSync(appContext)
        return Result.success()
    }

    private fun scheduleNextSync(context: Context) {
        // Schedule the work to repeat every day at the same time
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncManager>(1, TimeUnit.DAYS)
            .setInitialDelay(calculateInitialDelay(7, 30), TimeUnit.MILLISECONDS)  // Set the initial delay to the next 7:30 AM
            .build()

        // Enqueue the repeating sync request
        WorkManager.getInstance(context).enqueue(syncWorkRequest)
    }
}

fun calculateInitialDelay(targetHour: Int, targetMinute: Int): Long {
    val now = Calendar.getInstance()
    val targetTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, targetHour)
        set(Calendar.MINUTE, targetMinute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    // If the target time has already passed for today, set it for tomorrow
    if (targetTime.before(now)) {
        targetTime.add(Calendar.DAY_OF_MONTH, 1)
    }

    // Calculate the delay in milliseconds
    return targetTime.timeInMillis - now.timeInMillis
}

fun scheduleDailyWorkerSync(
    context: Context
) {
    val initialDelay = calculateInitialDelay(7, 30)  // Set target time to 7:30 AM
    val syncWorkRequest = OneTimeWorkRequestBuilder<SyncManager>()
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)  // Set the initial delay
        .build()

    // Enqueue the one-time sync request
    WorkManager.getInstance(context).enqueue(syncWorkRequest)

}