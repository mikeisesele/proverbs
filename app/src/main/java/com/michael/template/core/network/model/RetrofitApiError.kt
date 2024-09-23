package com.michael.template.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RetrofitApiError(@Json(name = "error") val error: String)
