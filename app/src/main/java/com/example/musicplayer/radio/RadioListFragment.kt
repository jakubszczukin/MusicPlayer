package com.example.musicplayer.radio

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.musicplayer.PLAYER_INTENT_MEDIA_ID
import com.example.musicplayer.PlayerActivity
import com.example.musicplayer.R

class RadioListFragment : Fragment() {


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
        val radioButton = view.findViewById(R.id.radioButton) as Button
        radioButton.setOnClickListener{
            val intent = Intent(activity?.baseContext, PlayerActivity::class.java)
            intent.putExtra(PLAYER_INTENT_MEDIA_ID, "http://streaming.radio.lublin.pl:8000/128k")
            startActivity(intent)
        }
    }

}