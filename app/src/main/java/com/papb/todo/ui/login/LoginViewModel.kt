package com.papb.todo.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.viewbinding.core.binding.livedata.liveData
import com.papb.todo.data.state.SimpleState
import com.papb.todo.root.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONStringer

class LoginViewModel : ViewModel() {
    val state: MutableLiveData<SimpleState> by liveData()

    fun postLogin(email: String, password: String) {
        val body: RequestBody
        val json = JSONStringer()
        json.`object`()
        json.key("email").value(email)
        json.key("password").value(password)
        json.endObject()
        body = json.toString()
            .toRequestBody(" application/json".toMediaTypeOrNull())

        App.endpoint.login(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<SimpleState>(SimpleState::Result)
            .onErrorReturn(SimpleState::Error)
            .toFlowable()
            .startWith(SimpleState.Loading)
            .subscribe(state::postValue)
            .let { return@let CompositeDisposable::add }
    }
}