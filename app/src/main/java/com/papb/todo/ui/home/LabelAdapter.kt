package com.papb.todo.ui.home

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oratakashi.viewbinding.core.binding.recyclerview.ViewHolder
import com.oratakashi.viewbinding.core.binding.recyclerview.viewBinding
import com.oratakashi.viewbinding.core.tools.onClick
import com.papb.todo.R
import com.papb.todo.data.model.label.DataLabel
import com.papb.todo.databinding.ListLabelBinding
import com.papb.todo.ui.todo.TodoInterface

class LabelAdapter(
    val inter: LabelInterface
) : RecyclerView.Adapter<ViewHolder<ListLabelBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<ListLabelBinding> = viewBinding(parent)

    override fun getItemCount(): Int = dataLabel.size

    override fun onBindViewHolder(holder: ViewHolder<ListLabelBinding>, position: Int) {
        with(holder.binding) {
            tvTitle.text = dataLabel[position].name_label
            tvTotal.text = dataLabel[position].total.toString()
            when (dataLabel[position].color_label) {
                "blue" -> {
                    ivTag.setColorFilter(ContextCompat.getColor(root.context, R.color.blue))
                }
                "green" -> {
                    ivTag.setColorFilter(ContextCompat.getColor(root.context, R.color.green))
                }
                "purple" -> {
                    ivTag.setColorFilter(ContextCompat.getColor(root.context, R.color.purple))
                }
                "red" -> {
                    ivTag.setColorFilter(ContextCompat.getColor(root.context, R.color.red))
                }
            }
            root.onClick { inter.onLongCLick(dataLabel[position]) }
        }
    }

    private var dataLabel: MutableList<DataLabel> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(data: List<DataLabel>) {
        this.dataLabel.clear()
        this.dataLabel.addAll(data)
        notifyDataSetChanged()
    }

}