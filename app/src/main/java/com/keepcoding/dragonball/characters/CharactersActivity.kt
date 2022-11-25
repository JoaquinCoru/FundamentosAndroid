package com.keepcoding.dragonball.characters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.keepcoding.dragonball.characters.list.ListFragment
import com.keepcoding.dragonball.databinding.ActivityCharactersBinding

class CharactersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharactersBinding
    private val viewModel:CharactersViewModel by viewModels()

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


      with(getSharedPreferences("SharedPreferences",Context.MODE_PRIVATE)) {
            val token = getString("Token", "")
            print("Token Activity $token")
            token?.let {
                viewModel.getCharacters(it)
            }
        }

        val fragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, fragment)
            .commitNow()


    }

}