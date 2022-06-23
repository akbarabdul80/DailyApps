package com.papb.todo.ui.main

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

class MainViewModel : ViewModel() {
    val stateGetDataLabel: MutableLiveData<SimpleState> by liveData()
    val stateGetDataTask: MutableLiveData<SimpleState> by liveData()
    val stateDeleteTask: MutableLiveData<SimpleState> by liveData()
    val stateDeleteLabel: MutableLiveData<SimpleState> by liveData()

    fun getLabel() {
        App.endpoint.getLabel()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<SimpleState>(SimpleState::Result)
            .onErrorReturn(SimpleState::Error)
            .toFlowable()
            .startWith(SimpleState.Loading)
            .subscribe(stateGetDataLabel::postValue)
            .let { return@let CompositeDisposable::add }
    }

    fun getTaskToday() {
        App.endpoint.getTaskToday()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<SimpleState>(SimpleState::Result)
            .onErrorReturn(SimpleState::Error)
            .toFlowable()
            .startWith(SimpleState.Loading)
            .subscribe(stateGetDataTask::postValue)
            .let { return@let CompositeDisposable::add }
    }

    fun deleteTask(id_task: String) {
        val body: RequestBody
        val json = JSONStringer()
        json.`object`()
        json.key("id_task").value(id_task.toInt())
        json.endObject()
        body = json.toString()
            .toRequestBody(" application/json".toMediaTypeOrNull())

        App.endpoint.deleteTask(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<SimpleState>(SimpleState::Result)
            .onErrorReturn(SimpleState::Error)
            .toFlowable()
            .startWith(SimpleState.Loading)
            .subscribe(stateDeleteTask::postValue)
            .let { return@let CompositeDisposable::add }
    }


    fun deleteLabel(id_label: String) {
        val body: RequestBody
        val json = JSONStringer()
        json.`object`()
        json.key("id_label").value(id_label.toInt())
        json.endObject()
        body = json.toString()
            .toRequestBody(" application/json".toMediaTypeOrNull())

        App.endpoint.deleteLabel(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<SimpleState>(SimpleState::Result)
            .onErrorReturn(SimpleState::Error)
            .toFlowable()
            .startWith(SimpleState.Loading)
            .subscribe(stateDeleteLabel::postValue)
            .let { return@let CompositeDisposable::add }
    }


}