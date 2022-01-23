package com.example.musicplayer.playlist

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.example.musicplayer.interfaces.OnItemClickListener
import com.squareup.picasso.Picasso


class PlaylistAdapter(private val context: Context, private val clickListener: OnItemClickListener) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {
    private var playlists: List<Playlist>? = null

    class ViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view){
        private val playlistName: TextView = view.findViewById(R.id.playlistName)
        private val playlistImage: ImageView = view.findViewById(R.id.playlistImage)

        fun bind(playlist: Playlist){
            playlistName.text = playlist.name
            playlistName.isSelected = true

            Picasso.get()
                .load(R.drawable.ic_baseline_music_video_24)
                .resize(96, 96)
                .centerCrop()
                .into(playlistImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_list_item, parent, false)

        return PlaylistAdapter.ViewHolder(view, context)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(pls: List<Playlist>){
        playlists = pls
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PlaylistAdapter.ViewHolder, position: Int) {
        playlists?.get(position)?.let{ playlist ->
            holder.bind(playlist)

            holder.itemView.setOnClickListener{
                clickListener.onItemClick(playlist.id)
            }
        }
    }

    override fun getItemCount(): Int = playlists?.size?: -1

}