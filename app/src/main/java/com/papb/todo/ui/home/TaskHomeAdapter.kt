package com.papb.todo.ui.home

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.viewbinding.core.binding.recyclerview.ViewHolder
import com.oratakashi.viewbinding.core.binding.recyclerview.viewBinding
import com.papb.todo.R
import com.papb.todo.data.model.task.DataTask
import com.papb.todo.databinding.ListTaskBinding
import com.papb.todo.utils.Converter

class TaskHomeAdapter : RecyclerView.Adapter<ViewHolder<ListTaskBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<ListTaskBinding> = viewBinding(parent)

    override fun getItemCount(): Int = dataLabel.size

    override fun onBindViewHolder(holder: ViewHolder<ListTaskBinding>, position: Int) {
        with(holder.binding) {
            tvTitle.text = dataLabel[position].name_task
            tvTag.text = dataLabel[position].label.name_label
            tvDate.text = Converter.dateTimeFormat(dataLabel[position].datetime)
            tvNote.text = dataLabel[position].note
            when (dataLabel[position].label.color_label) {
                "blue" -> {
                    ivTag.setColorFilter(getColor(root.context, R.color.blue))
                }
                "green" -> {
                    ivTag.setColorFilter(getColor(root.context, R.color.green))
                }
                "purple" -> {
                    ivTag.setColorFilter(getColor(root.context, R.color.purple))
                }
                "red" -> {
                    ivTag.setColorFilter(getColor(root.context, R.color.red))
                }
            }
        }
    }

    private var dataLabel: MutableList<DataTask> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(data: List<DataTask>) {
        this.dataLabel.clear()
        this.dataLabel.addAll(data)
        notifyDataSetChanged()
    }

}