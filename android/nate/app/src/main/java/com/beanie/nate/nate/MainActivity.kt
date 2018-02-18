package com.beanie.nate.nate

import android.app.*
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.NotificationCompat
import android.view.View
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveClient
import com.google.android.gms.drive.DriveFile
import com.google.android.gms.drive.DriveResourceClient
import com.google.android.gms.drive.query.Query
import com.google.android.gms.tasks.Task
import java.util.*

const val CHANNEL_ID = "my_channel_01"
const val CHANNEL_NAME = "nate channel"
const val CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH

const val NEW_NOTE_RESULT_CODE = 1
const val GOOGLE_AUTH_RESULT_CODE = 2

class MainActivity : AppCompatActivity() {
    private lateinit var notesListView : ListView
    private lateinit var addNoteButton : FloatingActionButton
    private lateinit var notificationChannel : NotificationChannel
    private lateinit var notificationManager : NotificationManager
    private lateinit var listAdapter : ArrayAdapter<String>
    private lateinit var account : GoogleSignInAccount
    private lateinit var driveClient : DriveClient
    private lateinit var driveResourceClient : DriveResourceClient

    private val localNotes = mutableListOf<String>()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notesListView = findViewById(R.id.notes_list)
        localNotes.add("Example Note 1")
        localNotes.add("Example Note 2")

        addNoteButton = findViewById(R.id.add_note)
        addNoteButton.setOnClickListener { view ->
            val intent = Intent(this, NewNoteActivity::class.java)
            startActivityForResult(intent, NEW_NOTE_RESULT_CODE)
        }

        notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, localNotes)
        notesListView.adapter = listAdapter

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Drive.SCOPE_APPFOLDER)
                .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        startActivityForResult(googleSignInClient.signInIntent, GOOGLE_AUTH_RESULT_CODE)

        notesListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            //val noteText = adapterView.getItemAtPosition(i) as? String ?: return@OnItemClickListener
            view.animate().setDuration(200).alpha(0F)
                    .withEndAction {
                        localNotes.removeAt(i)
                        listAdapter.notifyDataSetChanged()
                        view.setAlpha(1.0F)
                    }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            NEW_NOTE_RESULT_CODE ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    localNotes.add(data.dataString)
                    listAdapter.notifyDataSetChanged()
                }
            GOOGLE_AUTH_RESULT_CODE -> {
                account = GoogleSignIn.getSignedInAccountFromIntent(data)
                            .getResult(ApiException::class.java)
                driveClient = Drive.getDriveClient(this, account)
                driveResourceClient = Drive.getDriveResourceClient(this, account)
            }
        }
    }

    private fun getAllNotes() : Task<List<DriveFile>> =
        driveResourceClient.appFolder.continueWithTask { appFolderT ->
            val appFolder = appFolderT.result
            val query = Query.Builder().build()
            driveResourceClient.queryChildren(appFolder, query).continueWith { metaDatasT ->
                metaDatasT.result.map { it.driveId.asDriveFile() }
            }
        }

    //private fun fileToNote(driveFile : DriveFile)

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
