package com.papb.todo.ui.todo

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.viewbinding.core.binding.recyclerview.ViewHolder
import com.oratakashi.viewbinding.core.binding.recyclerview.viewBinding
import com.papb.todo.R
import com.papb.todo.data.model.task.DataTask
import com.papb.todo.databinding.ListTaskTodoBinding
import com.papb.todo.utils.Converter
import com.papb.todo.utils.ReminderUtils
import java.text.SimpleDateFormat
import java.util.*

class TaskTodoAdapter(
    val inter: TodoInterface
) : RecyclerView.Adapter<ViewHolder<ListTaskTodoBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<ListTaskTodoBinding> = viewBinding(parent)

    override fun getItemCount(): Int = dataLabel.size

    override fun onBindViewHolder(holder: ViewHolder<ListTaskTodoBinding>, position: Int) {
        with(holder.binding) {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val myCalendar = Calendar.getInstance()
            myCalendar.time = format.parse(dataLabel[position].datetime)

            ReminderUtils.workManagerPenyemangat(tvTitle.context, myCalendar, dataLabel[position])

            tvTitle.text = dataLabel[position].name_task
            tvTag.text = dataLabel[position].label.name_label
            tvDate.text = Converter.dateTimeFormat(dataLabel[position].datetime)

            tvTitle.setCompoundDrawablesWithIntrinsicBounds(
                if (dataLabel[position].done)
                    ContextCompat.getDrawable(
                        holder.binding.root.context,
                        R.drawable.ic_checked
                    )
                else
                    ContextCompat.getDrawable(
                        holder.binding.root.context,
                        R.drawable.ic_check
                    ),
                null,
                null,
                null
            )

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

            root.setOnClickListener {
                inter.onCLick(dataLabel[position])
            }

            root.setOnLongClickListener() {
                inter.onLongCLick(dataLabel[position])
                true
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