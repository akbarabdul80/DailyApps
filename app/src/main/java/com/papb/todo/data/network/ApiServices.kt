package com.papb.todo.data.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.papb.todo.BuildConfig
import com.papb.todo.data.network.interceptor.AuthInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ApiServices(val context: Context) {

    fun getInstance(): ApiEndpoint {
        val client = OkHttpClient().newBuilder().apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else
                    HttpLoggingInterceptor.Level.NONE
            })
            addInterceptor(AuthInterceptor())
            addInterceptor(ChuckerInterceptor(context))
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
        }

        val server =
            "http://192.168.8.103:8081/api/"
        return Retrofit.Builder()
            .baseUrl(server)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(ApiEndpoint::class.java)
    }
}