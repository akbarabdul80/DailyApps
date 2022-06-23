package com.papb.todo.data.model.user

import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    @SerializedName("status") val status: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("errors") val error: List<String>? = null,
    @SerializedName("data") val data: DataUser? = null
)
