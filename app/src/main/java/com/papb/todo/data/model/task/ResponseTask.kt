package com.papb.todo.data.model.task

import com.google.gson.annotations.SerializedName

data class ResponseTask(
    @SerializedName("status") val status: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("errors") val error: List<String>?,
    @SerializedName("data") val data: List<DataTask>? = null
)
