package com.papb.todo.data.model.label

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataLabel(
    @SerializedName("id_label") val id_label: Int,
    @SerializedName("name_label") val name_label: String,
    @SerializedName("num_task") val total: Int? = 0,
    @SerializedName("color_label") val color_label: String,
    var selected: Boolean = false
) : Parcelable