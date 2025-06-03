package com.example.lastbite

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationManager {

    fun generateNotificationChannel(context: Context, descriptionGiven: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                descriptionGiven,
                "Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = descriptionGiven
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun generateNotificationLeastVisitedStore(storeName: String, contextFragment: Context, channelID: String) :
            NotificationCompat.Builder {

        val builder = NotificationCompat.Builder(contextFragment, channelID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("$storeName is looking for you")
            .setContentText("You have forgotten them :(")
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText("$storeName is your least visited store. " +
                        "Why don't you check out what they have for you?"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        return builder
    }

    fun generateNotificationRevisitedPlace(contextFragment: Context, channelID: String) :
            NotificationCompat.Builder {

        val builder = NotificationCompat.Builder(contextFragment, channelID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("You really get hungry here, right?")
            .setContentText("The last time you visited this place you ordered something :P")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("See the stores available in the \"For You\" section!"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        return builder
    }
}