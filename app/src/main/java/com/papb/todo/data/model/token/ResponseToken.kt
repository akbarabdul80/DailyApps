package com.papb.todo.data.model.token

import com.google.gson.annotations.SerializedName
import com.papb.todo.data.model.user.DataUser

data class ResponseToken(
    @SerializedName("status") val status: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("errors") val error: List<String>?,
    @SerializedName("data") val data: DataUser? = null
)
