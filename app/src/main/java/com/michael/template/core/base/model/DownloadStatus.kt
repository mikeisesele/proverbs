package com.michael.template.core.base.model

data class DownloadStatus(
    val id: Long,
    val isSuccess: Boolean,
    val path: String?,
)
