package com.papb.todo.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.window.SplashScreen
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.papb.todo.R
import com.papb.todo.ui.splash.SplashActivity

class NotifPengingatManager(context: Context, params: WorkerParameters) : Worker(context, params) {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun doWork(): Result {
        val title = inputData.getString("title")
        val desc = inputData.getString("desc")
        sendNotification(0, title.toString(), desc.toString())

        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun sendNotification(id: Int, title: String, subtitle: String) {
        val intent = Intent(applicationContext, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title).setContentText(subtitle)
            .setDefaults(NotificationCompat.DEFAULT_ALL).setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL,
                    NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }

    companion object {
        const val NOTIFICATION_ID = "daily_task_notification_id"
        const val NOTIFICATION_NAME = "Daily Task Pengingat"
        const val NOTIFICATION_CHANNEL = "daily_task_channel_pengingat"
        const val NOTIFICATION_WORK_PENYEMANGAT = "pengingat_notification_work"
    }
}