package com.papb.todo.data.network.interceptor

import com.papb.todo.data.db.Sessions
import com.papb.todo.data.network.ApiEndpoint
import com.papb.todo.root.App
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection.HTTP_OK
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

class AuthInterceptor : Interceptor {
    private val mutex = Mutex()
    private val apiService by lazy {
        App.endpoint
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()

        if (ApiEndpoint.NO_AUTH in req.headers.values(ApiEndpoint.CUSTOM_HEADER)) {
            return chain.proceedWithToken(req, null)
        }

        val token = App.session.getData(Sessions.token)
        val res = chain.proceedWithToken(req, token)
        if (res.code != HTTP_UNAUTHORIZED) {
            return res
        }


        val newToken: String? = runBlocking(App.appDispatcher) {
            mutex.withLock {
                val user = App.session.getUser()
                val maybeUpdatedToken = user.token

                when {
                    maybeUpdatedToken == null -> null
                    maybeUpdatedToken != token -> maybeUpdatedToken
                    else -> {
                        val refreshTokenRes = apiService
                            .refreshToken(user.token, user.refreshToken!!)
                        when (refreshTokenRes.code()) {
                            HTTP_OK -> {
                                refreshTokenRes.body()!!.data!!.token.also { updatedToken ->
                                    updatedToken?.let { App.session.putData(Sessions.token, it) }
                                    refreshTokenRes.body()!!.data!!.refreshToken?.let {
                                        App.session.putData(
                                            Sessions.refresh_token,
                                            it
                                        )
                                        return@let
                                    }
                                }
                            }
                            HTTP_UNAUTHORIZED -> {
                                App.session.logout()
                                null
                            }
                            else -> {
                                null
                            }
                        }
                    }
                }
            }
        }

        return if (newToken !== null) {
            res.close()
            chain.proceedWithToken(req, newToken)
        } else {
            res
        }
    }

    private fun Interceptor.Chain.proceedWithToken(req: Request, token: String?): Response =
        req.newBuilder()
            .apply {
                if (token !== null) {
                    addHeader("Authorization", "$token")
                }
                addHeader("Accept", "application/json")
                addHeader("Content-Type", "application/json")
            }
            .removeHeader("@")
            .build()
            .let(::proceed)

}