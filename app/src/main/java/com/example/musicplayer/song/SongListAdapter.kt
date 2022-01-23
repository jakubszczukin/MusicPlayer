package com.example.musicplayer.song

import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.squareup.picasso.Picasso
import java.util.*
import android.widget.Toast
import com.example.musicplayer.interfaces.OnItemClickListener

class SongListAdapter(private val context: Context, private val clickListener: OnItemClickListener) : RecyclerView.Adapter<SongListAdapter.ViewHolder>() {

    private var songList: List<Song>? = null

    class ViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view){
        val songTitle: TextView = view.findViewById(R.id.songTitle)
        val songArtist: TextView = view.findViewById(R.id.songArtist)
        val songImage: ImageView = view.findViewById(R.id.songImage)
        val addSongToPlaylistButton: ImageView = view.findViewById(R.id.songAddToPlaylistButton)

        fun bind(song: Song){
            songTitle.text = song.title
            songArtist.text = song.artist

            Picasso.get()
                .load(song.artUri)
                .resize(96, 96)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_music_video_24)
                .error(R.drawable.ic_baseline_music_video_24)
                .into(songImage)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.songs_list_item, parent, false)

        return ViewHolder(view, context)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(songs: List<Song>){
        songList = songs
        notifyDataSetChanged()
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        songList?.get(position)?.let{ song ->
            holder.bind(song)

            val contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.id)

            holder.itemView.setOnClickListener{
                clickListener.onItemClick(contentUri, song.title)
            }

            holder.addSongToPlaylistButton.setOnClickListener {
                clickListener.onItemClick(song.id)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() : Int = songList?.size?: -1


}