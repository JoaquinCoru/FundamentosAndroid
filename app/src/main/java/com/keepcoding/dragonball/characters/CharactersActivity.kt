package com.keepcoding.dragonball.characters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.keepcoding.dragonball.databinding.ActivityCharactersBinding

class CharactersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharactersBinding

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, CharactersActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharactersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, fragment)
            .commitNow()


    }
}