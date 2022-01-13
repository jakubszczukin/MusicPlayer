package com.example.musicplayer.radio

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.PLAYER_INTENT_MEDIA_ID
import com.example.musicplayer.PLAYER_INTENT_MEDIA_NAME
import com.example.musicplayer.PlayerActivity
import com.example.musicplayer.R
import com.example.musicplayer.interfaces.OnItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RadioListFragment : Fragment(), OnItemClickListener {

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

        val getResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            var name = ""
            var uri = ""
            if(it.resultCode == Activity.RESULT_OK){
                it.data?.getStringExtra(RadioActivity.EXTRA_REPLY_NAME)?.let{ reply ->
                    name = reply
                }
                it.data?.getStringExtra(RadioActivity.EXTRA_REPLY_URI)?.let{ reply ->
                    uri = reply
                }
                val radio = Radio(name, uri)
                radioViewModel.insert(radio)
            }else{
                Toast.makeText(context, "Error while saving", Toast.LENGTH_LONG).show()
            }
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.radioRecyclerView)
        val adapter = RadioListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val noChannelsTextView = view.findViewById<TextView>(R.id.radioErrorTextView)

        radioViewModel.radioList.observe(viewLifecycleOwner) { radios ->
            if(radios.isEmpty())
                noChannelsTextView.visibility = View.VISIBLE
            else{
                noChannelsTextView.visibility = View.GONE
                radios.let{
                    adapter.submitList(it)
                }
            }
        }

        val fab = view.findViewById<FloatingActionButton>(R.id.floating_action_button)
        fab.setOnClickListener{
            val intent = Intent(activity?.applicationContext, RadioActivity::class.java)
            getResult.launch(intent)
        }

        /*
        val radioButton = view.findViewById(R.id.radioButton) as Button
        radioButton.setOnClickListener{
            val intent = Intent(activity?.baseContext, PlayerActivity::class.java)
            intent.putExtra(PLAYER_INTENT_MEDIA_ID, "http://streaming.radio.lublin.pl:8843/128k")
            startActivity(intent)
        }*/
    }

    override fun onItemClick(contentUri: Uri, contentName: String?) {
        Log.d("TEST", "ONITEMCLICK URI = {$contentUri}")
        val intent = Intent(activity?.baseContext, PlayerActivity::class.java)
        intent.putExtra(PLAYER_INTENT_MEDIA_ID, contentUri.toString())
        intent.putExtra(PLAYER_INTENT_MEDIA_NAME, contentName)
        startActivity(intent)
    }

    override fun onItemClick(id: Long) {
        TODO("Not yet implemented")
    }

    override fun onItemDeleteClick(id: Long) {
        radioViewModel.deleteById(id)
    }

}