package com.michael.proverbs.feature.proverbs.presentation.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.drawToBitmap
import com.michael.proverbs.core.ui.extensions.boldTexStyle
import com.michael.proverbs.feature.proverbs.domain.model.entity.VerseEntity
import java.io.File
import java.io.FileOutputStream
//
//@Composable
//fun CaptureProverbCardContent(
//    modifier: Modifier = Modifier,
//    currentRandomVerse: VerseEntity?
//) {
//    // Context to save the image
//    val context = LocalContext.current
//
//    // Use LocalView to get access to the root view
//    val view = LocalView.current
//
//    Box(
//        Modifier
//            .padding(24.dp)
//            .clip(RoundedCornerShape(16.dp))
//            .background(MaterialTheme.colorScheme.primary)
//            .fillMaxWidth()
//            .height(450.dp)
//    ) {
//        currentRandomVerse?.let {
//            Text(
//                it.verseText,
//                style = boldTexStyle(size = 18, color = Color.Black.copy(alpha = 0.8f)),
//                modifier = Modifier
//                    .align(Alignment.Center)
//                    .padding(horizontal = 18.dp),
//                textAlign = TextAlign.Center
//            )
//
//        }
//    }
//
//    // Function to capture and save the screenshot
//    fun captureAndShareScreenshot() {
//        val bitmap: Bitmap = view.drawToBitmap()
//
//        // Save the bitmap to a file or share
//        val file = File(context.cacheDir, "screenshot.png")
//        val fos = FileOutputStream(file)
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
//        fos.flush()
//        fos.close()
//
//        // You can now use the file for sharing
//        // Example: shareImage(file)
//    }
//}
