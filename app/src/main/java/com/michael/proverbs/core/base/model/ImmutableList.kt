package com.michael.proverbs.core.base.model

@androidx.compose.runtime.Immutable
data class ImmutableList<out T>(val data: List<T>) : List<T> by data

fun <T> List<T>.toImmutableList(): ImmutableList<T> = ImmutableList(this)

fun <T> immutableListOf(vararg elements: T): ImmutableList<T> =
    elements.asList().toImmutableList()

fun <T> emptyImmutableList(): ImmutableList<T> =
    emptyList<T>().toImmutableList()

inline fun <T, R : Comparable<R>> ImmutableList<T>.sortedBy(crossinline selector: (T) -> R?): ImmutableList<T> =
    data.sortedWith(compareBy(selector)).toImmutableList()

fun <T> ImmutableList<T>?.orEmpty(): ImmutableList<T> = this ?: emptyImmutableList()

fun ImmutableList<String>.joinIntoBrackets() = joinToString(
    separator = ", ",
    prefix = "[",
    postfix = "]",
)
