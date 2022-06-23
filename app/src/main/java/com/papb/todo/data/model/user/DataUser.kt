package com.papb.todo.data.model.user


import com.google.gson.annotations.SerializedName

data class DataUser(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("refresh_token")
    val refreshToken: String? = null,
    @SerializedName("token")
    val token: String? = null
)