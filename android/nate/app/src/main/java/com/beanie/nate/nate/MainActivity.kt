package com.beanie.nate.nate

import android.app.NotificationManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    lateinit var notesListView : ListView

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notesListView = findViewById(R.id.notes_list)

        val notes = arrayOf("Note 1", "Note 2")
        val listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes)
        notesListView.adapter = listAdapter
        notesListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            (adapterView.getItemAtPosition(i) as? String)?.let { item ->
                val notificationBuilder = NotificationCompat.Builder(this, "")
                        .setContentTitle("Nate")
                        .setContentText("You clicked $item")
                        .setSmallIcon(android.R.drawable.ic_delete)
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notification = notificationBuilder.build()
                notificationManager.notify(1, notification)
            }
        }
    }
}
