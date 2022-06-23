package com.papb.todo.data.model.label

import com.google.gson.annotations.SerializedName

data class ResponseLabel(
    @SerializedName("status") val status: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("errors") val error: List<String>? = null,
    @SerializedName("data") val data: List<DataLabel>? = null
)
