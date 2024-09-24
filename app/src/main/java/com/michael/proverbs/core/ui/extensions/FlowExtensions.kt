package com.michael.proverbs.core.ui.extensions

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.michael.proverbs.core.base.contract.SideEffect
import com.michael.proverbs.core.base.contract.ViewEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Suppress("TooGenericExceptionCaught")
@Composable
fun <T> rememberStateWithLifecycle(
    stateFlow: StateFlow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
): State<T> {
    val initialValue = remember(stateFlow) {
        stateFlow.value
    }
    return produceState(
        key1 = stateFlow,
        key2 = lifecycle,
        key3 = minActiveState,
        initialValue = initialValue,
    ) {
        lifecycle.repeatOnLifecycle(minActiveState) {
            stateFlow.collect {
                this@produceState.value = it
            }
        }
    }
}

/**
 * Create an effect that matches the lifecycle of the call site.
 * If LandingScreen recomposes, the delay shouldn't start again.
 *
 * https://stackoverflow.com/a/71626121/6510726
 */
@SuppressLint("ComposableNaming")
@Composable
fun <T> Flow<T>.collectAsEffect(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend (T) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        onEach(block).flowOn(context).launchIn(this)
    }
}

suspend inline fun <T : Any?> Flow<T>.collectBy(
    onStart: () -> Unit = {},
    crossinline onEach: (T) -> Unit = { _ -> },
    crossinline onError: (Throwable) -> Unit = { _ -> },
) {
    try {
        onStart()
        catch { error -> onError(error) }
            .distinctUntilChanged()
            .collect { item -> onEach(item) }
    } catch (e: Exception) {
        onError(e)
    }
}

suspend inline fun <T : Any?> Flow<T>.singleFlow(
    onStart: () -> Unit = {},
    crossinline onItemReceived: (T) -> Unit = { _ -> },
    crossinline onError: (Throwable) -> Unit = { _ -> },
) {
    try {
        onStart()
        flowOf(first())
            .catch { error -> onError(error) }
            .distinctUntilChanged()
            .collect { item -> onItemReceived(item) }
    } catch (e: Exception) {
        onError(e)
    }
}

suspend fun <T : Any?> Flow<T>.collectByWithScope(
    onStart: () -> Unit = {},
    onEach: suspend (T) -> Unit = { _ -> },
    onError: (Throwable) -> Unit = { _ -> },
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
) {
    try {
        onStart()
        catch { error -> onError(error) }
            .distinctUntilChanged()
            .collect { item ->
                coroutineScope.launch {
                    try {
                        onEach(item)
                    } catch (e: Exception) {
                        onError(e)
                    }
                }
            }
    } catch (e: Exception) {
        onError(e)
    }
}

suspend fun <T : Any?> Flow<T>.singleFlowOnItemReceivedInScope(
    onStart: () -> Unit = {},
    onItemReceived: suspend (T) -> Unit = { _ -> },
    onError: (Throwable) -> Unit = { _ -> },
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
) {
    try {
        onStart()
        firstOrNull()?.let { item ->
            coroutineScope.launch {
                try {
                    onItemReceived(item)
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }
    } catch (e: Exception) {
        onError(e)
    }
}


// Generic function to handle ViewEvent collection
suspend inline fun Flow<ViewEvent>.collectEffect(
    crossinline onEffect: (ViewEvent.Effect) -> Unit
) {
    // Collect the ViewEvent flow and filter for the specified effect type
    this
        .filterIsInstance<ViewEvent.Effect>()
        .map { it.effect }
        .filterIsInstance<ViewEvent.Effect>()
        .collect { effect ->
            onEffect(effect)
        }
}

suspend inline fun <reified T : SideEffect> Flow<ViewEvent>.collectAllEffect(
    crossinline onEffect: (T) -> Unit
) {
    this
        .filterIsInstance<ViewEvent.Effect>()
        .map { it.effect }
        .filterIsInstance<T>()
        .collect { effect ->
            onEffect(effect)
        }
}

suspend inline fun <reified T : SideEffect> Flow<ViewEvent>.collectAllNavigation(
    crossinline onEffect: (T) -> Unit
) {
    this
        .filterIsInstance<ViewEvent.Navigate>()
        .map { it.target }
        .filterIsInstance<T>()
        .collect { effect ->
            onEffect(effect)
        }
}

// Extension function on a function that returns Flow<SideEffect>
suspend inline fun <reified T : SideEffect> (() -> Flow<SideEffect>).collectSideEffect(
    crossinline onEffect: (T) -> Unit
) {
    // Invoke the function to get the Flow<SideEffect>
    val flow = this()
    // Collects only the effects of type T and applies the callback
    flow
        .filterIsInstance<T>()
        .collect { effect ->
            onEffect(effect)
        }
}

fun <T> List<T>.asFlow(): Flow<List<T>> =
    flow { emit(this@asFlow) }
        .distinctUntilChanged()

fun <T> T.asFlow(): Flow<T> =
    flowOf(this).distinctUntilChanged()
