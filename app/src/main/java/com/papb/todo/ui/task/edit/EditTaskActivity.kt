package com.papb.todo.ui.task.edit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.oratakashi.viewbinding.core.binding.activity.viewBinding
import com.oratakashi.viewbinding.core.binding.intent.intent
import com.oratakashi.viewbinding.core.tools.isNotNull
import com.oratakashi.viewbinding.core.tools.onClick
import com.oratakashi.viewbinding.core.tools.toast
import com.papb.todo.data.model.label.DataLabel
import com.papb.todo.data.model.label.ResponseLabel
import com.papb.todo.data.model.simple.ResponseSimple
import com.papb.todo.data.model.task.DataTask
import com.papb.todo.data.state.SimpleState
import com.papb.todo.databinding.ActivityEditTaskBinding
import com.papb.todo.ui.task.add.AddTaskAdapter
import com.papb.todo.utils.Converter
import com.papb.todo.utils.Validation
import dmax.dialog.SpotsDialog
import java.text.SimpleDateFormat
import java.util.*

class EditTaskActivity : AppCompatActivity() {
    private val binding: ActivityEditTaskBinding by viewBinding()
    private val taskViewModel: EditTaskViewModel by viewModels()
    private val taskAdapterLabel: AddTaskAdapter by lazy {
        AddTaskAdapter()
    }

    private val dataLabel: MutableList<DataLabel> = ArrayList()

    private val spotsDialog: SpotsDialog by lazy {
        SpotsDialog(this, "Mohon tunggu...")
    }

    private val data: DataTask by intent("data")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initListener()
        taskViewModel.getLabel()
        with(binding) {

            rvDataLabel.also {
                it.adapter = taskAdapterLabel
                it.layoutManager = GridLayoutManager(this@EditTaskActivity, 2)
            }
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val myCalendar = Calendar.getInstance()
            myCalendar.time = format.parse(data.datetime)

            etTanggal.setText(Converter.dateTimeFormat(data.datetime))

            etTanggal.onClick {
                DatePickerDialog(
                    this@EditTaskActivity,
                    { _, year, monthOfYear, dayOfMonth ->
                        myCalendar.set(Calendar.YEAR, year)
                        myCalendar.set(Calendar.MONTH, monthOfYear)
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val sdf =
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                        TimePickerDialog(
                            this@EditTaskActivity,
                            { view, hourOfDay, minute ->
                                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                myCalendar.set(Calendar.MINUTE, minute)
                                etTanggal.setText(sdf.format(myCalendar.time))
                            },
                            myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),
                            true
                        ).show()


                    },
                    myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            taskAdapterLabel.submitData(dataLabel)

            btnBack.onClick {
                finish()
            }

            btnSubmit.onClick {
                if (Validation.validateEditText(
                        listOf(
                            etName,
                            etTanggal,
                            etCatatan
                        )
                    ) && taskAdapterLabel.getSelected().isNotNull()
                ) {
                    val sdf =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    taskViewModel.editTask(
                        DataTask(
                            data.id_task,
                            etName.text.toString(),
                            sdf.format(myCalendar.time),
                            etCatatan.text.toString(),
                            false,
                            taskAdapterLabel.getSelected()!!
                        )
                    )
                }
            }

            etName.setText(data.name_task)
            etCatatan.setText(data.note)
        }
    }

    private fun initListener() {
        taskViewModel.stateGetDataLabel.observe(
            this
        ) { simpleState: SimpleState? ->
            if (simpleState is SimpleState.Loading) {
                binding.rvDataLabel.visibility = View.GONE
            } else if (simpleState is SimpleState.Result<*>) {
                binding.rvDataLabel.visibility = View.VISIBLE
                if (simpleState.data is ResponseLabel) {
                    taskAdapterLabel.submitData(simpleState.data.data!!)
                }
            } else if (simpleState is SimpleState.Error) {
                binding.rvDataLabel.visibility = View.GONE
                toast("Gagal meengambil data")
            }
        }

        taskViewModel.stateEditDataTask.observe(this) {
            when (it) {
                SimpleState.Loading -> {
                    spotsDialog.show()
                }
                is SimpleState.Result<*> -> {
                    spotsDialog.dismiss()
                    if (it.data is ResponseSimple) {
                        toast(it.data.message!!)
                        finish()
                    }
                }
                is SimpleState.Error -> {
                    spotsDialog.dismiss()
                }
            }
        }
    }
}