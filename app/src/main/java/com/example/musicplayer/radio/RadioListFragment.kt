package com.example.musicplayer.radio

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.musicplayer.PLAYER_INTENT_MEDIA_ID
import com.example.musicplayer.PlayerActivity
import com.example.musicplayer.R
import com.example.musicplayer.TestRadioActivity
import com.example.musicplayer.database.AppDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RadioListFragment : Fragment() {

    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        var name: String
        var id: Long
        var uri: Uri
        if(it.resultCode == Activity.RESULT_OK){
            it.data?.getLongExtra("ID", 0)?.let{ reply ->
                id = reply
            }
            it.data?.getStringExtra("NAME")?.let{ reply ->
                name = reply
            }
            it.data?.getStringExtra("URI")?.let{ reply ->
                uri = Uri.parse(reply)
            }
        }else{
            Toast.makeText(context, "Blad podczas zapisywania", Toast.LENGTH_LONG).show()
        }
    }


    private val radioViewModel: RadioViewModel by viewModels{
        RadioViewModel.RadioViewModelFactory((activity?.application as RadioApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_radio_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.radioRecyclerView)
        val adapter = RadioListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        radioViewModel.radioList.observe(viewLifecycleOwner) { radios ->
            radios.let{
                adapter.submitList(it)
            }
        }

        val fab = view.findViewById<FloatingActionButton>(R.id.floating_action_button)
        fab.setOnClickListener{
            val intent = Intent(activity?.applicationContext, TestRadioActivity::class.java)
            getResult.launch(intent)
        }

        /*
        val radioButton = view.findViewById(R.id.radioButton) as Button
        radioButton.setOnClickListener{
            val intent = Intent(activity?.baseContext, PlayerActivity::class.java)
            intent.putExtra(PLAYER_INTENT_MEDIA_ID, "http://streaming.radio.lublin.pl:8000/128k")
            startActivity(intent)
        }*/
    }

}