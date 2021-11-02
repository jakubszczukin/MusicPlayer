package com.example.musicplayer.song

import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.R
import com.mtechviral.mplaylib.MusicFinder


class SongListFragment : Fragment() {

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
        songListAdapter = SongListAdapter(requireActivity().applicationContext)
        songsRecyclerView.layoutManager = LinearLayoutManager(view.context)

        songsRecyclerView.adapter = songListAdapter

    }

    private fun loadSongs() {
        SongObserver.getInstance(requireContext()).findSongs()
            .observe(viewLifecycleOwner, Observer { songs ->
                songListAdapter.setData(songs)
            })
    }

}