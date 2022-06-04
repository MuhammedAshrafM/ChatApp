package com.meem.domain.base

import kotlinx.serialization.Serializable

@Serializable
data class ResponseApi<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)