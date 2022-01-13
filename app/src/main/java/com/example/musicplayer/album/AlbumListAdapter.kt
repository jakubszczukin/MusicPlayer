package com.example.musicplayer.album

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.media.Image
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.interfaces.OnItemClickListener
import com.example.musicplayer.song.Song
import com.example.musicplayer.song.SongListAdapter
import com.squareup.picasso.Picasso

class AlbumListAdapter(private val context: Context, private val clickListener: OnItemClickListener) : RecyclerView.Adapter<AlbumListAdapter.ViewHolder>() {
    private var albumList: List<Album>? = null

    class ViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view){
        private val albumTitle: TextView = view.findViewById(R.id.albumTitle)
        private val albumArtist: TextView = view.findViewById(R.id.albumArtist)
        private val albumImage: ImageView = view.findViewById(R.id.albumImage)

        fun bind(album: Album){
            albumTitle.text = album.name
            albumArtist.text = album.artist

            albumTitle.isSelected = true

            Picasso.get()
                .load(album.coverUri)
                .resize(96, 96)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_music_video_24)
                .error(R.drawable.ic_baseline_music_video_24)
                .into(albumImage)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumListAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.albums_list_item, parent, false)

        return AlbumListAdapter.ViewHolder(view, context)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(albums: List<Album>){
        albumList = albums
        notifyDataSetChanged()
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: AlbumListAdapter.ViewHolder, position: Int) {
        albumList?.get(position)?.let{ album ->
            holder.bind(album)

            val contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, album.id)

            holder.itemView.setOnClickListener{
                clickListener.onItemClick(album.id)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() : Int = albumList?.size?: -1
}