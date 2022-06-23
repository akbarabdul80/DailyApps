package com.papb.todo.utils

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.papb.todo.data.model.task.DataTask
import com.papb.todo.workmanager.NotifPengingatManager
import com.papb.todo.workmanager.NotifPengingatManager.Companion.NOTIFICATION_WORK_PENYEMANGAT
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

object ReminderUtils {
    fun workManagerPenyemangat(context: Context, dueDate: Calendar, dataTask: DataTask) {
        val currentDate = Calendar.getInstance()
        if (dueDate.after(currentDate)) {
            val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

            val data = Data.Builder()
            data.putString("title", "Pengingat ${dataTask.name_task}")
            data.putString("desc", "Today ${Converter.dateTimeFormat(dataTask.datetime)}")

            val dailyWorkRequest = OneTimeWorkRequest.Builder(NotifPengingatManager::class.java)
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .addTag(NOTIFICATION_WORK_PENYEMANGAT)
                .setInputData(data.build())
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    dataTask.id_task.toString(),
                    ExistingWorkPolicy.REPLACE,
                    dailyWorkRequest
                )
        }
    }
}