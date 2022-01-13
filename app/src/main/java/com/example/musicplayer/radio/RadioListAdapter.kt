package com.example.musicplayer.radio

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.musicplayer.R
import com.example.musicplayer.database.AppDatabase
import com.example.musicplayer.interfaces.OnItemClickListener
import com.example.musicplayer.song.SongListAdapter

class RadioListAdapter(private val clickListener: OnItemClickListener) : ListAdapter<Radio, RadioListAdapter.RadioViewHolder>(RadioComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioViewHolder {
        return RadioViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RadioViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name)

        holder.itemView.setOnClickListener{
            clickListener.onItemClick(Uri.parse(current.uri), current.name)
        }

        holder.radioDeleteButton.setOnClickListener{
            clickListener.onItemDeleteClick(current.id)
        }

    }

    class RadioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val radioName: TextView = itemView.findViewById(R.id.radioName)
        val radioDeleteButton: ImageView = itemView.findViewById(R.id.radioDeleteButton)

        fun bind(text: String?){
            radioName.text = text
        }

        companion object{
            fun create(parent: ViewGroup): RadioViewHolder{
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.radio_list_item, parent, false)
                return RadioViewHolder(view)
            }
        }
    }

    class RadioComparator: DiffUtil.ItemCallback<Radio>(){
        override fun areItemsTheSame(oldItem: Radio, newItem: Radio): Boolean{
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Radio, newItem: Radio): Boolean{
            return oldItem.name == newItem.name
        }
    }
}