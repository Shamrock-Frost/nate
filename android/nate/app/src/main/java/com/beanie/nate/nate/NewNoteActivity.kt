package com.beanie.nate.nate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class NewNoteActivity : AppCompatActivity() {
    private lateinit var noteText : EditText
    private lateinit var confirm : Button
    private lateinit var cancel : Button

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        noteText = findViewById(R.id.note_text)

        confirm = findViewById(R.id.confirm)
        confirm.setOnClickListener {
            val data = Intent()

            data.setData(Uri.parse(noteText.getText().toString()))
            setResult(Activity.RESULT_OK, data)

            finish()
        }

        cancel = findViewById(R.id.cancel)
        cancel.setOnClickListener {
            val data = Intent()

            data.setData(Uri.parse("fail"))
            setResult(Activity.RESULT_CANCELED, data)

            finish()
        }
    }
}
