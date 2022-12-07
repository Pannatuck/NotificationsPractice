package com.pan.notificationspractice

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService


class MainActivity : AppCompatActivity() {

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIFICATION_ID = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        val intent = Intent(this, MainActivity::class.java)
        // для того, щоб користувач міг відкрити програму за кліком по повідомленню, треба передати
        // pendingIntent в системний сервіс, який займається відображення повідомлень
        val pendingIntent = TaskStackBuilder.create(this).run {
            // додає актівіті в backstack. Тобто, якщо користувач нажме кнопку Back на телефоні,
            // він повернеться назад, до програми, яка була запущенна раніше
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        // створюємо саме повідомлення, яке буде відображатись в Channel
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("David, my guy!")
            .setContentText("Awesome notification")
            .setSmallIcon(R.drawable.ic_notification_image)
            // це пріорітет між сповіщеннями самої апки. Відрізняється від пріорітету каналу тим,
            // що там йде пріорітет між різними повідомленнями з усієї системи (ну і чи потрібно
            // це відобразити візуально та зі звуком, чи просто додати його до шторки)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // додаємо створений вишче pendingIntent в системний builder повідомлень
            .setContentIntent(pendingIntent)
            .build()

        // це вже менеджер для самого сповіщення
        val notificationManager = NotificationManagerCompat.from(this)

        val btnShowNotification = findViewById<Button>(R.id.btnShow)
        btnShowNotification.setOnClickListener{
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // створюємо channel, який буде відпопідати цьому сповіщенню
            // (в андроід кожне сповіщення регулюється своїм каналом)
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                // про оцей пріорітет кажу вище
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                    // просто для прикладу обрано блімкання діодом біля передньої камери
                    lightColor = Color.GREEN
                    enableLights(true)
            }
            // треба явно вказати тип, тому що getSystemService повертає тип Any (сервісів багато різних)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}