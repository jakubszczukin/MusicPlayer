package com.example.musicplayer.radio

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.example.musicplayer.R
import com.google.android.material.switchmaterial.SwitchMaterial

class RadioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radio)

        val topAppBar = findViewById<Toolbar>(R.id.topAppBar)
        topAppBar.setNavigationOnClickListener{
            val replyIntent = Intent()
            setResult(Activity.RESULT_CANCELED, replyIntent)
            finish()
        }


        // Getting stuff from inputs and sending them to the database
        val finishButton : Button = findViewById(R.id.radioFinishButton)
        finishButton.setOnClickListener {
            val radioName = findViewById<EditText>(R.id.radioName)
            val radioUri = findViewById<EditText>(R.id.radioUri)
            val replyIntent = Intent()

            when {
                TextUtils.isEmpty(radioName.text) -> radioName.error = "Radio name is required!"
                TextUtils.isEmpty(radioUri.text) -> radioUri.error = "Radio URL is required!"
                else -> {
                    replyIntent.putExtra(EXTRA_REPLY_NAME, radioName.text.toString())
                    replyIntent.putExtra(EXTRA_REPLY_URI, radioUri.text.toString())
                    setResult(Activity.RESULT_OK, replyIntent)
                    finish()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Initialize and inflate menu for action bar
        menuInflater.inflate(R.menu.action_bar_menu, menu)

        val themeSwitch = findViewById<SwitchMaterial>(R.id.dayNightSwitch)
        themeSwitch.setOnCheckedChangeListener{ _, id ->
            when(id){
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    // Init vals in companion object to access them in another class (make them class level property, not instance level)
    companion object{
        const val EXTRA_REPLY_NAME = "com.example.musicplayer.radio.RADIONAMEREPLY"
        const val EXTRA_REPLY_URI = "com.example.musicplayer.radio.RADIOURIREPLY"
    }
}