package com.example.musicplayer.song

import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.MainActivity
import com.example.musicplayer.PLAYER_INTENT_MEDIA_ID
import com.example.musicplayer.PlayerActivity
import com.example.musicplayer.R
import com.example.musicplayer.interfaces.OnItemClickListener


class SongListFragment : Fragment(), OnItemClickListener {

    private lateinit var songsRecyclerView: RecyclerView
    private lateinit var songListAdapter: SongListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSongs()

        songsRecyclerView = view.findViewById(R.id.songsRecyclerView)
        songListAdapter = SongListAdapter(requireActivity().applicationContext, this)
        songsRecyclerView.layoutManager = LinearLayoutManager(view.context)

        songsRecyclerView.adapter = songListAdapter

    }

    private fun loadSongs() {
        SongObserver.getInstance(requireContext()).findSongs()
            .observe(viewLifecycleOwner, Observer { songs ->
                songListAdapter.setData(songs)
            })
    }

    override fun onItemClick(contentUri: Uri) {
        val intent = Intent(activity?.baseContext, PlayerActivity::class.java)
        intent.putExtra(PLAYER_INTENT_MEDIA_ID, contentUri.toString())
        startActivity(intent)
    }

}