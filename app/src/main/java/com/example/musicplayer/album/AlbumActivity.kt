package com.example.musicplayer.album

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.musicplayer.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.switchmaterial.SwitchMaterial

class AlbumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

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
}