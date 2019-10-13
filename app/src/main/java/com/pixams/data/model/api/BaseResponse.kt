package com.pixams.data.model.api

import com.google.gson.annotations.SerializedName

/**
 * @author Kaiser Perwez
 */

data class BaseResponse<out T>(
    @SerializedName("status")
    val status: Boolean? = false,
    @SerializedName("msg")
    val message: String? = "",
    val data: T?,
    val error: Throwable?
)
