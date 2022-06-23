package com.papb.todo.ui.todo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oratakashi.viewbinding.core.binding.livedata.liveData
import com.papb.todo.data.model.task.DataTask
import com.papb.todo.data.state.SimpleState
import com.papb.todo.root.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONStringer

class TodoViewModel : ViewModel() {
    val stateGetDataTask: MutableLiveData<SimpleState> by liveData()
    val stateUpdateDataTask: MutableLiveData<SimpleState> by liveData()

    fun getTask() {
        App.endpoint.getTask()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<SimpleState>(SimpleState::Result)
            .onErrorReturn(SimpleState::Error)
            .toFlowable()
            .startWith(SimpleState.Loading)
            .subscribe(stateGetDataTask::postValue)
            .let { return@let CompositeDisposable::add }
    }

    fun updateTask(data: DataTask) {
        val body: RequestBody
        val json = JSONStringer()
        json.`object`()
        json.key("name_task").value(data.name_task)
        json.key("note").value(data.note)
        json.key("datetime").value(data.datetime)
        json.key("label_id").value(data.label.id_label)
        json.key("id_task").value(data.id_task)
        json.key("done").value(data.done)
        json.endObject()
        body = json.toString()
            .toRequestBody(" application/json".toMediaTypeOrNull())

        App.endpoint.updateTask(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<SimpleState>(SimpleState::Result)
            .onErrorReturn(SimpleState::Error)
            .toFlowable()
            .startWith(SimpleState.Loading)
            .subscribe(stateUpdateDataTask::postValue)
            .let { return@let CompositeDisposable::add }
    }

}