package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.mtechviral.mplaylib.MusicFinder

class SongsAdapter(private val context: Context, var songTitles : List<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return songTitles.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //var songView = context.getLayoutInflater().inflate(R.layout.list_item, null)
        val songView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val textSong = songView.findViewById(R.id.songName) as TextView
        textSong.isSelected = true
        textSong.text = songTitles.elementAt(position)

        return songView
    }

}