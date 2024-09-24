package com.michael.proverbs.core.common

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.michael.easylog.logE

fun displayToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun Dp.toPx(): Int = LocalDensity.current.run {
    roundToPx()
}


fun Context.readFromAssets(fileName: String) : String {
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