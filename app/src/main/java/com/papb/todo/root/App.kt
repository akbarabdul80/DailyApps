package com.papb.todo.root

import android.app.Application
import com.papb.todo.data.db.Sessions
import com.papb.todo.data.network.ApiEndpoint
import com.papb.todo.data.network.ApiServices
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel

class App : Application() {
    companion object {
        lateinit var session: Sessions
        lateinit var endpoint: ApiEndpoint
        lateinit var appDispatcher: CoroutineDispatcher
    }

    override fun onCreate() {
        super.onCreate()
        session = Sessions(this)
        appDispatcher = IO
        endpoint = ApiServices(this).getInstance()
    }

}