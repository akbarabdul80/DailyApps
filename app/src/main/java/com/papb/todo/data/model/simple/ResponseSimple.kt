package com.papb.todo.data.model.simple

import com.google.gson.annotations.SerializedName

data class ResponseSimple(
    @SerializedName("status") val status: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("errors") val error: List<String>?,
)