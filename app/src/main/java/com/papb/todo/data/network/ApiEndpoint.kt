package com.papb.todo.data.network

import com.papb.todo.data.model.label.ResponseLabel
import com.papb.todo.data.model.simple.ResponseSimple
import com.papb.todo.data.model.task.ResponseTask
import com.papb.todo.data.model.token.ResponseToken
import com.papb.todo.data.model.user.ResponseLogin
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiEndpoint {

    companion object Factory {
        const val CUSTOM_HEADER = "@"
        const val NO_AUTH = "NoAuth"
    }

    /**
     * Login
     */
    @Headers("$CUSTOM_HEADER: $NO_AUTH")
    @POST("auth/login")
    fun login(
        @Body body: RequestBody
    ): Single<ResponseLogin>

    /**
     * Refresh Token
     */
    @Headers("$CUSTOM_HEADER: $NO_AUTH")
    @GET("auth/refresh-token")
    suspend fun refreshToken(
        @Header("Authorization") auth: String,
        @Header("Authorization_Refresh") auth_refresh: String,
    ): Response<ResponseToken>

    /**
     * Get Label
     */
    @GET("user/label")
    fun getLabel(
    ): Single<ResponseLabel>


    /**
     * Add Label
     */
    @PUT("user/label")
    fun addLabel(
        @Body body: RequestBody
    ): Single<ResponseSimple>

    /**
     * Edit Label
     */
    @PATCH("user/label")
    fun editLabel(
        @Body body: RequestBody
    ): Single<ResponseSimple>

    /**
     * Add Task
     */
    @PUT("user/task")
    fun addTask(
        @Body body: RequestBody
    ): Single<ResponseSimple>

    /**
     * Edit Task
     */
    @PATCH("user/task")
    fun editTask(
        @Body body: RequestBody
    ): Single<ResponseSimple>

    /**
     * Get Task
     */
    @GET("user/task")
    fun getTask(
    ): Single<ResponseTask>

    /**
     * Get Task Today
     */
    @GET("user/task-today")
    fun getTaskToday(
    ): Single<ResponseTask>

    /**
     * Update Task
     */
    @PATCH("user/task")
    fun updateTask(
        @Body body: RequestBody
    ): Single<ResponseSimple>

    /**
     * dalete Task
     */
    @HTTP(method = "DELETE", path = "user/task", hasBody = true)
    fun deleteTask(
        @Body body: RequestBody
    ): Single<ResponseSimple>

    /**
     * dalete Label
     */
    @HTTP(method = "DELETE", path = "user/label", hasBody = true)
    fun deleteLabel(
        @Body body: RequestBody
    ): Single<ResponseSimple>

}