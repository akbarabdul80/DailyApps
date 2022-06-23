package com.papb.todo.data.model.task

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.papb.todo.data.model.label.DataLabel
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataTask(
    @SerializedName("id_task") val id_task: Int,
    @SerializedName("name_task") val name_task: String,
    @SerializedName("datetime") val datetime: String,
    @SerializedName("note") val note: String,
    @SerializedName("done") var done: Boolean,
    @SerializedName("label") var label: DataLabel,
) : Parcelable