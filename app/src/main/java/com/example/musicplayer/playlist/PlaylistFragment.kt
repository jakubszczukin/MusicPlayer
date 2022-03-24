package com.example.musicplayer.playlist

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.PLAYER_INTENT_MEDIA_ID
import com.example.musicplayer.R
import com.example.musicplayer.album.AlbumListAdapter
import com.example.musicplayer.album.AlbumObserver
import com.example.musicplayer.interfaces.OnItemClickListener
import com.example.musicplayer.song.Song

private lateinit var playlistsRecyclerView: RecyclerView

class PlaylistFragment : Fragment(), OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistsRecyclerView = view.findViewById(R.id.playlistsRecyclerView)
        val playlistsAdapter = PlaylistAdapter(requireActivity().applicationContext, this)
        playlistsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        playlistsRecyclerView.adapter = playlistsAdapter
        loadPlaylists(playlistsAdapter)

        val dividerItemDecoration = DividerItemDecoration(
            playlistsRecyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        playlistsRecyclerView.addItemDecoration(dividerItemDecoration)

    }

    private fun loadPlaylists(playlistsAdapter: PlaylistAdapter){
        PlaylistObserver.getInstance(requireContext()).findPlaylists()
            .observe(viewLifecycleOwner, Observer{ playlists ->
                val noPlaylistsTextView = view?.findViewById(R.id.playlistErrorTextView) as TextView
                if(playlists.isEmpty()){
                    noPlaylistsTextView.visibility = VISIBLE
                }
                else {
                    noPlaylistsTextView.visibility = GONE
                    playlistsAdapter.setData(playlists)
                }
            })
    }

    override fun onItemClick(id: Long, uri: Uri, name: String?) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(contentUri: Uri, contentName: String?) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(id: Long) {
        val intent = Intent(activity?.baseContext, PlaylistSongsActivity::class.java)
        intent.putExtra(PLAYER_INTENT_MEDIA_ID, id.toString())
        startActivity(intent)
    }

    override fun onItemDeleteClick(id: Long) {
        TODO("Not yet implemented")
    }
}