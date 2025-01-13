package com.michael.proverbs.feature.entrypoint

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import com.michael.kompanion.utils.kompanionAllNotNull
import com.michael.proverbs.core.common.displayToast
import com.michael.proverbs.core.ui.theme.TemplateTheme
import com.michael.proverbs.feature.notification.scheduleDailyAlarm
import com.michael.proverbs.feature.notification.scheduleDailyWorkerSync
import com.michael.proverbs.feature.proverbs.presentation.ProverbsScreen
import com.michael.proverbs.feature.proverbs.presentation.ProverbsScreenDestination
import com.michael.proverbs.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    companion object {
        private const val REQUEST_CODE_POST_NOTIFICATIONS = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Check POST_NOTIFICATIONS permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request it
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_POST_NOTIFICATIONS
                )
            } else {
                // Already granted; schedule the alarm
                scheduleDailyAlarm(this)
            }
        } else {
            // Android 12 and below: no need for POST_NOTIFICATIONS permission
            scheduleDailyAlarm(this)
        }

        setContent {
            navController = rememberNavController()

            TemplateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 3) Compose Navigation
                    Navigator(navController, closeApp = {
                        finish()
                    })
                }
            }
            // 2) Handle any extras if the app was launched by a notification
            handleDeepLink(intent)
        }
    }

    /**
     * Called when the user responds to the permission request dialog.
     */
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Schedule the alarm after permission is granted
                scheduleDailyAlarm(this)
            } else {
                displayToast(this, "Please grant notification permission to use this feature.")
            }
        }
    }

    /**
     * Called if the Activity is already alive and a new intent (e.g., tapping the notification again) comes in.
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            handleDeepLink(it)
        }
    }

    /**
     * Extract the extras from the intent (if any) and navigate accordingly.
     */
    private fun handleDeepLink(
        intent: Intent,
    ) {
        val verseText = intent.getStringExtra("VERSE_TEXT")
        val chapterNumber = intent.getStringExtra("CHAPTER_NUMBER")
        val verseNumber = intent.getStringExtra("VERSE_NUMBER")

        // 1) If all 3 are non-null, we know we came from a verse notification
        if (kompanionAllNotNull(
                verseText,
                chapterNumber,
                verseNumber
            )
        ) {
            // 2) Navigate to your desired screen
            // Option A: If you have a route like "verseDetail/{chapter}/{verse}/{text}"
            navController.navigate(
                ProverbsScreenDestination(
                    title = verseText,
                    chapter = chapterNumber,
                    verse = verseNumber
                )
            )


            // 3) Clear the extras so subsequent config changes don’t repeatedly navigate
            intent.removeExtra("VERSE_TEXT")
            intent.removeExtra("CHAPTER_NUMBER")
            intent.removeExtra("VERSE_NUMBER")
        }
    }
}
