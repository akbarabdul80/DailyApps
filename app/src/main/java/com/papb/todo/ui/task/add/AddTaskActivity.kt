package com.papb.todo.ui.task.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.oratakashi.viewbinding.core.binding.activity.viewBinding
import com.oratakashi.viewbinding.core.tools.isNotNull
import com.oratakashi.viewbinding.core.tools.onClick
import com.oratakashi.viewbinding.core.tools.toast
import com.papb.todo.data.model.label.DataLabel
import com.papb.todo.data.model.label.ResponseLabel
import com.papb.todo.data.model.simple.ResponseSimple
import com.papb.todo.data.model.task.DataTask
import com.papb.todo.data.state.SimpleState
import com.papb.todo.data.state.SimpleState.Loading
import com.papb.todo.databinding.ActivityAddBinding
import com.papb.todo.utils.Validation
import dmax.dialog.SpotsDialog
import java.text.SimpleDateFormat
import java.util.*


class AddTaskActivity : AppCompatActivity() {
    private val binding: ActivityAddBinding by viewBinding()
    private val taskViewModel: AddTaskViewModel by viewModels()
    private val taskAdapterLabel: AddTaskAdapter by lazy {
        AddTaskAdapter()
    }

    private val dataLabel: MutableList<DataLabel> = ArrayList()

    private val spotsDialog: SpotsDialog by lazy {
        SpotsDialog(this, "Mohon tunggu...")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initListener()
            taskViewModel.getLabel()
        with(binding) {
            rvDataLabel.also {
                it.adapter = taskAdapterLabel
                it.layoutManager = GridLayoutManager(this@AddTaskActivity, 2)
            }

            val myCalendar = Calendar.getInstance()

            etTanggal.onClick {
                DatePickerDialog(
                    this@AddTaskActivity,
                    { _, year, monthOfYear, dayOfMonth ->
                        myCalendar.set(Calendar.YEAR, year)
                        myCalendar.set(Calendar.MONTH, monthOfYear)
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val sdf =
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                        TimePickerDialog(this@AddTaskActivity,
                            { view, hourOfDay, minute ->
                                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                myCalendar.set(Calendar.MINUTE, minute)
                                etTanggal.setText(sdf.format(myCalendar.time))
                            },
                            myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE),
                            false
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
                            etCatatan,
                        )
                    ) && taskAdapterLabel.getSelected().isNotNull()
                ) {
                    val sdf =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    taskViewModel.addTask(
                        DataTask(
                            0,
                            etName.text.toString(),
                            sdf.format(myCalendar.time),
                            etCatatan.text.toString(),
                            false,
                            taskAdapterLabel.getSelected()!!
                        )
                    )
                }
            }

        }

    }


    private fun initListener() {
        taskViewModel.stateGetDataLabel.observe(
            this
        ) { simpleState: SimpleState? ->
            if (simpleState is Loading) {
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

        taskViewModel.stateAddDataTask.observe(this) {
            when (it) {
                Loading -> {
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