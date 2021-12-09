package com.example.musicplayer.album

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.MainActivity
import com.example.musicplayer.PLAYER_INTENT_MEDIA_ID
import com.example.musicplayer.PlayerActivity
import com.example.musicplayer.R
import com.example.musicplayer.interfaces.OnItemClickListener
import com.example.musicplayer.song.SongListAdapter

class AlbumListFragment : Fragment(), OnItemClickListener {

    private lateinit var albumsRecyclerView: RecyclerView
    private lateinit var albumListAdapter: AlbumListAdapter
    private lateinit var albumErrorTextView: TextView
    private var isAlbumListEmpty = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadAlbums()
        albumsRecyclerView = view.findViewById(R.id.albumsRecyclerView)
        albumListAdapter = AlbumListAdapter(requireActivity().applicationContext, this)
        albumsRecyclerView.layoutManager = LinearLayoutManager(view.context)

        albumsRecyclerView.adapter = albumListAdapter

        val dividerItemDecoration = DividerItemDecoration(
            albumsRecyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        albumsRecyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun loadAlbums(){
        AlbumObserver.getInstance(requireContext()).findAlbums()
            .observe(viewLifecycleOwner, Observer { albums ->
                if(albums == null)
                    isAlbumListEmpty = true
                else{
                    isAlbumListEmpty = false
                    albumListAdapter.setData(albums)
                }
            })
    }

    override fun onItemClick(contentUri: Uri){
        Toast.makeText(context, "Album clicked -> TODO", Toast.LENGTH_SHORT).show()
        //val intent = Intent(activity?.baseContext, PlayerActivity::class.java)
        //intent.putExtra(PLAYER_INTENT_MEDIA_ID, contentUri.toString())
        //startActivity(intent)
    }

}