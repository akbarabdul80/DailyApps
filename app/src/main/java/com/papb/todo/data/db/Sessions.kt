package com.papb.todo.data.db

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.papb.todo.BuildConfig
import com.papb.todo.data.model.user.DataUser


@SuppressLint("CommitPrefEdits")
class Sessions(context: Context) {
    companion object {
        var PREF_NAME = BuildConfig.APPLICATION_ID + ".session"

        const val id: String = "id"
        const val name: String = "name"
        const val email: String = "email"
        const val token: String = "token"
        const val refresh_token: String = "refresh_token"
    }

    var masterKey: MasterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

    var pref: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREF_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var editor = pref.edit()

    var context: Context? = null

    init {
        this.context = context
        pref = EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        editor = pref.edit()
    }

    fun putData(key: String, value: String) {
        editor!!.putString(key, value)
        editor!!.commit()
    }

    fun putData(key: String, value: Boolean) {
        editor!!.putBoolean(key, value)
        editor!!.commit()
    }

    fun getData(key: String): String {
        return pref.getString(key, "").toString()
    }

    fun getBoolean(key: String): Boolean {
        return pref.getBoolean(key, true)
    }

    fun isLogin(): Boolean {
        return getData(id).isNotEmpty()
    }

    fun doLogin(data: DataUser?) {
        if (data != null) pref.edit {
            putString(id, data.id.toString())
            putString(name, data.name)
            putString(email, data.email)
            putString(token, data.token)
            putString(refresh_token, data.refreshToken)
        }
    }


    fun getUser() : DataUser {
        return DataUser(
            getData(email),
            getData(id).toIntOrNull(),
            getData(name),
            getData(refresh_token),
            getData(token)
        )
    }

    fun logout() {
        editor!!.clear()
        editor!!.commit()
    }
}