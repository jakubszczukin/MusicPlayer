package com.example.musicplayer.song

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


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

        permissionHandler(requireActivity().applicationContext)

        songsRecyclerView = view.findViewById(R.id.songsRecyclerView)
        songListAdapter = SongListAdapter(requireActivity().applicationContext, this)
        songsRecyclerView.layoutManager = LinearLayoutManager(view.context)

        songsRecyclerView.adapter = songListAdapter

    }

    private fun permissionHandler(context : Context){

        Dexter.withContext(context)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                // Permissions Granted
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    loadSongs()
                }

                // Permissions Denied
                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(context, "Permissions denied!", Toast.LENGTH_SHORT).show()
                    showSettingsDialog(context)
                }

                // What to do if user rejected permissions before
                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                    token.continuePermissionRequest()
                    showSettingsDialog(context)
                }
            }).onSameThread().check()
    }


    private fun showSettingsDialog(context : Context){
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Need Permissions")

        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, _ ->
            dialog!!.cancel()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.data = uri
            @Suppress("DEPRECATION")
            startActivityForResult(intent, 101)
        }
        //builder.setNegativeButton("Cancel"){ dialog, _ ->
        //    dialog!!.cancel()
        //}

        builder.show()
    }

    private fun loadSongs() {
        SongObserver.getInstance(requireContext()).findSongs()
            .observe(viewLifecycleOwner, Observer { songs ->
                val noSongsTextView = view?.findViewById(R.id.songErrorTextView) as TextView
                if(songs.isEmpty())
                    noSongsTextView.visibility = VISIBLE
                else{
                    noSongsTextView.visibility = GONE
                    songListAdapter.setData(songs)
                }
            })
    }

    override fun onItemClick(contentUri: Uri) {
        val intent = Intent(activity?.baseContext, PlayerActivity::class.java)
        intent.putExtra(PLAYER_INTENT_MEDIA_ID, contentUri.toString())
        startActivity(intent)
    }

}