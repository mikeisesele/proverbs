package com.michael.template.core.ui.util

import com.michael.template.core.base.providers.StringProvider
import retrofit2.HttpException
import javax.inject.Inject

interface ErrorHandler {

    fun getErrorMessage(error: Throwable): String
}

class ErrorHandlerImpl @Inject constructor(
    private val stringProvider: StringProvider,
) : ErrorHandler {

    override fun getErrorMessage(error: Throwable): String {
        return when (error) {
            is HttpException -> "Network failed"
            else -> "Something Went Wrong"
        }
    }
}
