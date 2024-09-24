package com.michael.proverbs.core.base.model

data class DownloadStatus(
    val id: Long,
    val isSuccess: Boolean,
    val path: String?,
)
