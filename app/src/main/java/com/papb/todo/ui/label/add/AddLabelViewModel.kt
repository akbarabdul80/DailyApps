package com.papb.todo.ui.label.add

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

class AddLabelViewModel : ViewModel() {
    val stateAddLabel: MutableLiveData<SimpleState> by liveData()

    fun addLabel(name: String, color: String) {
        val body: RequestBody
        val json = JSONStringer()
        json.`object`()
        json.key("name_label").value(name)
        json.key("color_label").value(color)
        json.endObject()
        body = json.toString()
            .toRequestBody(" application/json".toMediaTypeOrNull())

        App.endpoint.addLabel(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<SimpleState>(SimpleState::Result)
            .onErrorReturn(SimpleState::Error)
            .toFlowable()
            .startWith(SimpleState.Loading)
            .subscribe(stateAddLabel::postValue)
            .let { return@let CompositeDisposable::add }
    }

}