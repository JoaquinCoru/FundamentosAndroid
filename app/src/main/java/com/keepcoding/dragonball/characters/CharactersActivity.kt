package com.keepcoding.dragonball.characters

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.databinding.ActivityCharactersBinding

class CharactersActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityCharactersBinding

    companion object{

        fun launch(context:Context){
            val intent = Intent(context, CharactersActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharactersBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}