package com.michael.proverbs.core.common

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.michael.easylog.logE
import com.michael.proverbs.core.base.util.titleCase
import java.util.Locale

fun displayToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun Dp.toPx(): Int = LocalDensity.current.run {
    roundToPx()
}

// Helper function to convert Dp to pixels
fun Dp.toPx(density: Density): Float = with(density) { this@toPx.toPx() }

fun String.customTitleCase(): String {
    val src = this
    return buildString {
        if (src.isNotEmpty()) {
            append(src[0].uppercase(Locale.getDefault()))
        }
        if (src.length > 1) {
            append(src.substring(1).lowercase(Locale.getDefault()))
        }
    }
}

//fun String.sentenceCase(): String {
//    val src = this
//
//    return src.split(" ")
//        .joinToString(" ") { it.customTitleCase() }
//}

fun String.sentenceCase(): String {
    // Trim leading and trailing spaces, just to clean up
    val trimmed = this.trim()
    if (trimmed.isEmpty()) return trimmed

    // Convert entire string to lowercase
    val lowerText = trimmed.lowercase()

    val result = StringBuilder()

    // We want to capitalize the first letter immediately
    var shouldCapitalizeNext = true

    for (char in lowerText) {
        if (shouldCapitalizeNext && char.isLetter()) {
            // Capitalize this character
            result.append(char.uppercaseChar())
            // Reset the flag
            shouldCapitalizeNext = false
        } else {
            result.append(char)
        }

        // If we see a period, then we should capitalize the next letter
        if (char == '.' || char == '!' || char == '?') {
            shouldCapitalizeNext = true
        }
    }

    return result.toString()
}




fun Activity.readFromAssets(fileName: String) : String {
    return safeReturnableOperation (
        operation = {
            assets.open(fileName)
                .bufferedReader()
                .use {
                    with(it)  {
                        readText()
                    }
                }
        },
        exceptionMessage = "ReadFromAssets Error"
    ).orEmpty()
}

fun Context.readFromRawResource(resourceId: Int): String {
    return resources.openRawResource(resourceId).bufferedReader().use { it.readText() }
}

/**
 * Safely executes an operation by wrapping it in a try-catch block. The operation to be invoked
 * must have a nullable return type.
 *
 * @param operation The operation to be executed, returning a nullable result.
 * @param exceptionMessage The log message associated with the exception.
 * @param actionOnException An optional action to be executed in case of an exception.
 *                          It takes a nullable string parameter representing the error message.
 * @return The result of the operation or null in case of an exception.
 */
fun <T> safeReturnableOperation(
    operation: () -> T?,
    actionOnException: ((message: Exception?) -> Unit)? = null,
    exceptionMessage: String,
): T? {
    return try {
        operation.invoke()
    } catch (e: Exception) {
        //print stack trace
        e.printStackTrace()

        exceptionMessage.logE()

        // Invoke the optional actionOnException with the error message
        actionOnException?.invoke(e)

        // Return null in case of an exception
        null
    }
}