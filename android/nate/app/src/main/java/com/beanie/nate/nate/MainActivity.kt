package com.beanie.nate.nate

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView

const val CHANNEL_ID = "my_channel_01"
const val CHANNEL_NAME = "nate channel"
const val CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH

class MainActivity : AppCompatActivity() {
    private lateinit var notesListView : ListView
    private lateinit var notificationChannel : NotificationChannel
    private lateinit var notificationManager : NotificationManager

    private val localNotes = mutableListOf<String>()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notesListView = findViewById(R.id.notes_list)
        localNotes.add("Example Note 1")
        localNotes.add("Example Note 2")

        notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        val listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, localNotes)
        notesListView.adapter = listAdapter
        notesListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val noteText = adapterView.getItemAtPosition(i) as? String ?: return@OnItemClickListener

            sendNotification(title = "You clicked a note",
                             text = "It said \"$noteText\"",
                             icon = android.R.drawable.ic_media_play)
        }
    }

    private fun sendNotification(title : String, text : String, icon : Int,
                                notifyID : Int = 1,
                                intent : PendingIntent? = null) {
        val builder = Notification.Builder(this@MainActivity, "")
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(icon)
                .setChannelId(CHANNEL_ID)
        intent?.let { builder.setContentIntent(it) }
        val notification = builder.build()
        notificationManager.notify(notifyID, notification)
    }
}
