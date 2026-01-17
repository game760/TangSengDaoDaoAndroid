package com.daodao.openim

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ForegroundService : Service() {
    // 通知渠道ID
    private companion object {
        private const val CHANNEL_ID = "AppForegroundChannel"
        private const val NOTIFICATION_ID = 1001
    }

    override fun onCreate() {
        super.onCreate()
        // 创建通知渠道
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 构建前台服务通知（常驻通知栏）
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.foreground_service_title))
            .setContentText(getString(R.string.foreground_service_content))
            .setSmallIcon(R.mipmap.ic_logo)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()

        // 启动为前台服务
        startForeground(NOTIFICATION_ID, notification)

        // 返回START_STICKY
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // 无需绑定，返回null
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // 停止前台服务（仅手动停止时触发）
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    // 创建通知渠道
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "前台服务", // 渠道名称
                NotificationManager.IMPORTANCE_LOW // 低优先级，不弹窗打扰
            )
            // 注册渠道到系统
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}